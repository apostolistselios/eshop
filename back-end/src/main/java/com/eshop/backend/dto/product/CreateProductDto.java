package com.eshop.backend.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductDto(
        @NotBlank
        @Size(max = 100)
        String brand,

        @NotBlank
        @Size(max = 100)
        String type,

        String description,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        @Digits(integer = 12, fraction = 2)
        BigDecimal price,

        @NotNull
        @Min(0)
        @Max(1_000_000)
        Long quantity
) {
}
