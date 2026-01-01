package com.eshop.backend.services;

import com.eshop.backend.dto.SignupCustomerDto;
import com.eshop.backend.dto.SignupShopDto;
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
        User shopUser = this.userService.createShopUser(data.getEmail(), data.getPassword());
        Shop newShop = this.shopService.createShop(data.getTin(), data.getBrandName(), data.getOwner(), shopUser);

        return newShop;
    }

    @Transactional
    public Customer signupAsCustomer(SignupCustomerDto data) {
        User customerUser = this.userService.createCustomerUser(data.getEmail(), data.getPassword());
        Customer newCustomer = this.customerService.createCustomer(
                data.getTin(), data.getFirstname(), data.getLastname(), data.getEmail(), customerUser);

        return newCustomer;
    }
}
