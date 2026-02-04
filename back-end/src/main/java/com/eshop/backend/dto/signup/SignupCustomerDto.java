package com.eshop.backend.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupCustomerDto(
        @NotBlank
        @Size(min = 9, max = 9)
        String tin,

        @NotBlank
        @Size(max = 80)
        String firstname,

        @NotBlank
        @Size(max = 80)
        String lastname,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
