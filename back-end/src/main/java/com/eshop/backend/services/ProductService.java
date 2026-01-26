package com.eshop.backend.services;

import com.eshop.backend.dto.CreateProductDto;
import com.eshop.backend.dto.UpdateProductDto;
import com.eshop.backend.exceptions.NotFoundException;
import com.eshop.backend.exceptions.ProductAlreadyExistsException;
import com.eshop.backend.models.Product;
import com.eshop.backend.models.Shop;
import com.eshop.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
