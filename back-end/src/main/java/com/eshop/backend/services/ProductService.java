package com.eshop.backend.services;

import com.eshop.backend.dto.product.CreateProductDto;
import com.eshop.backend.dto.product.UpdateProductDto;
import com.eshop.backend.exceptions.NotEnoughStockException;
import com.eshop.backend.exceptions.NotFoundException;
import com.eshop.backend.exceptions.ProductAlreadyExistsException;
import com.eshop.backend.models.Cart;
import com.eshop.backend.models.CartItem;
import com.eshop.backend.models.Product;
import com.eshop.backend.models.Shop;
import com.eshop.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    public Page<Product> getProducts(
            String type,
            String brand,
            String description,
            Double minPrice,
            Double maxPrice,
            Integer minQuantity,
            Integer maxQuantity,
            Pageable pageable
    ) {
        return this.productRepository.findByFilters(
                type,
                brand,
                description,
                minPrice,
                maxPrice,
                minQuantity,
                maxQuantity,
                pageable);
    }

    public Page<Product> getShopProducts(
            String type,
            String brand,
            String description,
            Double minPrice,
            Double maxPrice,
            Integer minQuantity,
            Integer maxQuantity,
            Pageable pageable
    ) {
        Shop shop = this.shopService.findByCurrentUser();

        return this.productRepository.findShopProductsByFilters(
                shop.getId(),
                type,
                brand,
                description,
                minPrice,
                maxPrice,
                minQuantity,
                maxQuantity,
                pageable);
    }

    public Product getProductForShop(Long id) {
        Shop shop = this.shopService.findByCurrentUser();

        return this.productRepository.findByIdAndShop(id, shop)
                .orElseThrow(() -> new NotFoundException("Product with id: " + id + " for shop: " + shop.getTin() + " found."));
    }

    public Product createProductForShop(CreateProductDto data) {
        if (this.productRepository.existsByBrandAndType(data.brand(), data.type())) {
            throw new ProductAlreadyExistsException(data.brand(), data.type());
        }

        Shop shop = this.shopService.findByCurrentUser();

        Product product = new Product();
        product.setType(data.type());
        product.setBrand(data.brand());
        product.setDescription(data.description());
        product.setPrice(data.price());
        product.setQuantity(data.quantity());
        product.setShop(shop);

        return this.productRepository.save(product);
    }

    public Product updateProductForShop(Long id, UpdateProductDto data) {
        Product product = this.getProductForShop(id);

        if (data.description() != null) {
            product.setDescription(data.description());
        }
        if (data.brand() != null) {
            product.setBrand(data.brand());
        }
        if (data.type() != null) {
            product.setType(data.type());
        }
        if (data.price() != null) {
            product.setPrice(data.price());
        }
        if (data.quantity() != null) {
            product.setQuantity(data.quantity());
        }

        return this.productRepository.save(product);
    }

    public void deleteProductForShop(Long id) {
        Shop shop = this.shopService.findByCurrentUser();

        Optional<Product> product = this.productRepository.findByIdAndShop(id, shop);
        if (!product.isPresent()) {
            throw new NotFoundException("Product with id: " + id + " for shop: " + shop.getTin() + " found.");
        }

        this.productRepository.delete(product.get());
    }

    public Map<Long, Product> loadProductsForCartWithLock(Cart cart) {
        Set<Long> ids = extractProductIdsFromCart(cart);
        List<Product> products = this.productRepository.findAllByIdInForUpdate(ids);
        Map<Long, Product> map = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        for (Long id : ids) {
            if (!map.containsKey(id)) {
                throw new NotFoundException("Product not found: " + id);
            }
        }

        return map;
    }

    public void checkProductsHaveEnoughStock(Cart cart, Map<Long, Product> products) {
        for (CartItem cartItem : cart.getItems()) {
            Product product = products.get(cartItem.getProduct().getId());
            Long requestedQty = cartItem.getQuantity();
            Long availableQty = product.getQuantity() == null ? 0 : product.getQuantity();
            if (requestedQty > availableQty) {
                throw new NotEnoughStockException(product);
            }
        }
    }

    public void decrementQuantitiesBasedOnCart(Cart cart, Map<Long, Product> products) {
        for (CartItem cartItem : cart.getItems()) {
            Product product = products.get(cartItem.getProduct().getId());
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            this.productRepository.save(product);
        }
    }

    public Product findProductById(Long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with id: " + productId + " not found"));
    }

    private Set<Long> extractProductIdsFromCart(Cart cart) {
        if (cart.getItems() == null) {
            return Collections.emptySet();
        }

        return cart.getItems().stream()
                .map(i -> i.getProduct().getId())
                .collect(Collectors.toSet());
    }
}
