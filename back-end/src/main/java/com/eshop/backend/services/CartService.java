package com.eshop.backend.services;

import com.eshop.backend.exceptions.BadRequestException;
import com.eshop.backend.exceptions.NotEnoughStockException;
import com.eshop.backend.exceptions.NotFoundException;
import com.eshop.backend.models.*;
import com.eshop.backend.repositories.CartItemRepository;
import com.eshop.backend.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    public Cart getCartByCustomer() {
        Customer customer = this.customerService.findByCurrentUser();
        return this.cartRepository.findWithItemsByCustomer(customer)
                .orElseGet(() -> this.createCartForCustomer(customer));
    }

    @Transactional
    public Cart addCartItem(Long productId, Long quantity) {
        Cart cart = this.getCartByCustomer();
        Product product = this.productService.findProductById(productId);
        CartItem item = this.cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(0L);
                    cartItem.setUnitPrice(product.getPrice());

                    cart.getItems().add(cartItem);
                    cartItem.setCart(cart);

                    return cartItem;
                });

        Long newQty = item.getQuantity() + quantity;

        if (product.getQuantity() != null && newQty > product.getQuantity()) {
            throw new NotEnoughStockException(product);
        }

        item.setQuantity(newQty);
        this.cartItemRepository.save(item);

        return cart;
    }

    @Transactional
    public Cart updateCartItem(Long productId, Long quantity) {
        if (quantity < 0) {
            throw new BadRequestException("quantity must be >= 0");
        }

        Cart cart = this.getCartByCustomer();
        CartItem item = this.cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if (quantity == 0) {
            this.cartItemRepository.delete(item);
        } else {
            Product product = item.getProduct();
            if (product.getQuantity() != null && quantity > product.getQuantity()) {
                throw new NotEnoughStockException(product);
            }
            item.setQuantity(quantity);
            this.cartItemRepository.save(item);
        }

        return this.getCartByCustomer();
    }

    @Transactional
    public PurchaseOrder checkout() {
        Cart cart = this.getCartByCustomer();
        this.assertCartIsNotEmpty(cart);

        Map<Long, Product> products = this.productService.loadProductsForCartWithLock(cart);
        this.productService.checkProductsHaveEnoughStock(cart, products);
        PurchaseOrder order = this.orderService.createOrder(cart);
        this.productService.decrementQuantitiesBasedOnCart(cart, products);
        this.cartItemRepository.deleteByCartId(cart.getId());

        return order;
    }

    private Cart createCartForCustomer(Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);

        return this.cartRepository.save(cart);
    }

    private void assertCartIsNotEmpty(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
    }
}
