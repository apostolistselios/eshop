package com.eshop.backend.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemDto(
        @NotNull
        Long productId,

        @NotNull
        @Min(1)
        Long quantity
) {
}
