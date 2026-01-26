package com.eshop.backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductDto(
        @Size(max = 255)
        String description,

        @Size(max = 100)
        String brand,

        @Size(max = 100)
        String type,

        @DecimalMin(value = "0.0", inclusive = true)
        @Digits(integer = 12, fraction = 2)
        BigDecimal price,

        @Min(value = 0)
        @Max(value = 1_000_000)
        Long quantity
) {
}
