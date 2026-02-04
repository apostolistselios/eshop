package com.eshop.backend.mappers;

import com.eshop.backend.dto.cart.CartItemResponseDto;
import com.eshop.backend.dto.cart.CartResponseDto;
import com.eshop.backend.models.Cart;
import com.eshop.backend.models.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartMapper {
    public static CartResponseDto toDto(Cart cart) {
        List<CartItemResponseDto> items = cart.getItems().stream()
                .map(CartMapper::toItemDto)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponseDto::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDto(
                cart.getId(),
                items,
                total
        );
    }

    private static CartItemResponseDto toItemDto(CartItem item) {
        BigDecimal unitPrice = item.getUnitPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponseDto(
                item.getProduct().getId(),
                item.getProduct().getBrand(),
                item.getProduct().getType(),
                unitPrice,
                item.getQuantity(),
                totalPrice
        );
    }
}
