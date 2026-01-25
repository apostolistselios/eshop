package com.eshop.backend.controllers;

import com.eshop.backend.dto.SignupCustomerDto;
import com.eshop.backend.dto.SignupShopDto;
import com.eshop.backend.models.Customer;
import com.eshop.backend.models.Shop;
import com.eshop.backend.services.SignupService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public Shop signupAsShop(@RequestBody SignupShopDto data) {
        return this.signupService.signupAsShop(data);
    }

    @PostMapping(path = "customer")
    public Customer signupAsCustomer(@RequestBody SignupCustomerDto data) {
        return this.signupService.signupAsCustomer(data);
    }
}
