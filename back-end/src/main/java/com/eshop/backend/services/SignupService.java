package com.eshop.backend.services;

import com.eshop.backend.dto.signup.SignupCustomerDto;
import com.eshop.backend.dto.signup.SignupShopDto;
import com.eshop.backend.models.Customer;
import com.eshop.backend.models.Shop;
import com.eshop.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {
    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CustomerService customerService;

    @Transactional
    public Shop signupAsShop(SignupShopDto data) {
        User shopUser = this.userService.createShopUser(data.email(), data.password());
        Shop newShop = this.shopService.createShop(data.tin(), data.brandName(), data.owner(), shopUser);

        return newShop;
    }

    @Transactional
    public Customer signupAsCustomer(SignupCustomerDto data) {
        User customerUser = this.userService.createCustomerUser(data.email(), data.password());
        Customer newCustomer = this.customerService.createCustomer(
                data.tin(), data.firstname(), data.lastname(), data.email(), customerUser);

        return newCustomer;
    }
}
