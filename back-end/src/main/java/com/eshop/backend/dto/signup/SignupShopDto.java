package com.eshop.backend.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupShopDto(
        @NotBlank
        @Size(min = 9, max = 9)
        String tin,

        @NotBlank
        @Size(max = 160)
        String brandName,

        @NotBlank
        @Size(max = 160)
        String owner,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
