package com.eshop.backend.dto.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDto(
        Long id,
        List<OrderItemResponseDto> items,
        BigDecimal totalAmount
) {
}
