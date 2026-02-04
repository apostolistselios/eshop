package com.eshop.backend.controllers;

import com.eshop.backend.dto.cart.AddCartItemDto;
import com.eshop.backend.dto.cart.CartResponseDto;
import com.eshop.backend.dto.cart.UpdateCartItemDto;
import com.eshop.backend.dto.order.OrderResponseDto;
import com.eshop.backend.mappers.CartMapper;
import com.eshop.backend.mappers.OrderMapper;
import com.eshop.backend.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart")
@RestController
@RequestMapping(path = "cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public CartResponseDto getCart() {
        return CartMapper.toDto(this.cartService.getCartByCustomer());
    }

    @PostMapping(path = "item")
    public CartResponseDto addCartItem(@Valid @RequestBody AddCartItemDto data) {
        return CartMapper.toDto(this.cartService.addCartItem(data.productId(), data.quantity()));
    }

    @PatchMapping(path = "item/{productId}")
    public CartResponseDto updateCartItem(@PathVariable Long productId, @Valid @RequestBody UpdateCartItemDto data) {
        return CartMapper.toDto(this.cartService.updateCartItem(productId, data.quantity()));
    }

    @DeleteMapping(path = "item/{productId}")
    public void deleteCartItem(@PathVariable Long productId) {
        this.cartService.deleteCartItem(productId);
    }

    @PostMapping(path = "checkout")
    public OrderResponseDto checkoutCart() {
        return OrderMapper.toDto(this.cartService.checkout());
    }
}
