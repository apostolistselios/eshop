package com.eshop.backend.dto.cart;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemDto(
        @NotNull
        @Min(0)
        Long quantity
) {
}