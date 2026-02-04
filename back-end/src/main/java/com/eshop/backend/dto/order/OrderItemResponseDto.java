package com.eshop.backend.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        String productBrand,
        String productType,
        BigDecimal unitPrice,
        Long quantity,
        BigDecimal totalPrice
) {
}
