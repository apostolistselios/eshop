package com.eshop.backend.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "signup")
public class SignupController {
    @PostMapping(path = "customer")
    public void loginAsCustomer(Authentication authentication) {
        System.out.println(authentication);
    }
}
