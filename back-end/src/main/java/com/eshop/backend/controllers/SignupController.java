package com.eshop.backend.controllers;

import com.eshop.backend.dto.signup.SignupCustomerDto;
import com.eshop.backend.dto.signup.SignupShopDto;
import com.eshop.backend.models.Customer;
import com.eshop.backend.models.Shop;
import com.eshop.backend.services.SignupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping(path = "signup")
public class SignupController {
    @Autowired
    private SignupService signupService;

    @PostMapping(path = "shop")
    public Shop signupAsShop(@Valid @RequestBody SignupShopDto data) {
        return this.signupService.signupAsShop(data);
    }

    @PostMapping(path = "customer")
    public Customer signupAsCustomer(@Valid @RequestBody SignupCustomerDto data) {
        return this.signupService.signupAsCustomer(data);
    }
}
