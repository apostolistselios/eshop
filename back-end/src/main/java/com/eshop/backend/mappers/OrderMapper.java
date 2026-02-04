package com.eshop.backend.mappers;

import com.eshop.backend.dto.order.OrderItemResponseDto;
import com.eshop.backend.dto.order.OrderResponseDto;
import com.eshop.backend.models.OrderItem;
import com.eshop.backend.models.PurchaseOrder;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {
    public static OrderResponseDto toDto(PurchaseOrder order) {
        List<OrderItemResponseDto> items = order.getItems().stream()
                .map(OrderMapper::toItemDto)
                .toList();

        BigDecimal total = items.stream()
                .map(OrderItemResponseDto::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponseDto(
                order.getId(),
                items,
                total
        );
    }

    private static OrderItemResponseDto toItemDto(OrderItem item) {
        BigDecimal unitPrice = item.getUnitPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponseDto(
                item.getProduct().getId(),
                item.getProduct().getBrand(),
                item.getProduct().getType(),
                unitPrice,
                item.getQuantity(),
                totalPrice
        );
    }
}
