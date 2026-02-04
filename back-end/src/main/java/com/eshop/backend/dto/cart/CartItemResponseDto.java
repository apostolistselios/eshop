package com.eshop.backend.dto.cart;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long productId,
        String productBrand,
        String productType,
        BigDecimal unitPrice,
        Long quantity,
        BigDecimal totalPrice
) {
}
