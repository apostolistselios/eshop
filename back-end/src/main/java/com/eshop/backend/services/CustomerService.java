package com.eshop.backend.services;

import com.eshop.backend.exceptions.CustomerAlreadyExistsException;
import com.eshop.backend.exceptions.NotFoundException;
import com.eshop.backend.models.Customer;
import com.eshop.backend.models.User;
import com.eshop.backend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    public Customer createCustomer(String tin, String firstname, String lastname, String email, User user) {
        this.assertTinDoesNotExist(tin);

        Customer customer = new Customer();
        customer.setTin(tin);
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);
        customer.setUser(user);

        return this.customerRepository.save(customer);
    }

    public Customer findByCurrentUser() {
        User user = this.userService.getCurrentUser();

        return this.customerRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Customer not found for user"));
    }

    private void assertTinDoesNotExist(String tin) {
        if (this.customerRepository.existsByTin(tin)) {
            throw new CustomerAlreadyExistsException(tin);
        }
    }
}
