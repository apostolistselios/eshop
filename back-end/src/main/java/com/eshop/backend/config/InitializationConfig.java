package com.eshop.backend.config;

import com.eshop.backend.constants.Roles;
import com.eshop.backend.models.Customer;
import com.eshop.backend.models.Role;
import com.eshop.backend.models.Shop;
import com.eshop.backend.models.User;
import com.eshop.backend.repositories.CustomerRepository;
import com.eshop.backend.repositories.RoleRepository;
import com.eshop.backend.repositories.ShopRepository;
import com.eshop.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class InitializationConfig implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initializeRoles();
        this.initializeUsers();
    }

    private void initializeRoles() {
        Optional<Role> customerRole = this.roleRepository.findByName(Roles.CUSTOMER);
        if (!customerRole.isPresent()) {
            this.roleRepository.save(new Role(Roles.CUSTOMER));
        }

        Optional<Role> shopRole = this.roleRepository.findByName(Roles.SHOP);
        if (!shopRole.isPresent()) {
            this.roleRepository.save(new Role(Roles.SHOP));
        }
    }

    private void initializeUsers() {
        if (!userRepository.existsByEmail("shop@demo.com")) {
            User shopUser = new User();
            shopUser.setEmail("shop@demo.com");
            shopUser.setPassword(passwordEncoder.encode("shop"));

            Role shopRole = this.roleRepository.findByName(Roles.SHOP)
                    .orElseThrow(() -> new RuntimeException("Role SHOP must exists to initialize users."));
            shopUser.setRole(shopRole);

            userRepository.save(shopUser);

            Shop shop = new Shop();
            shop.setTin("999999999");
            shop.setBrandName("Brand Name");
            shop.setOwner("Owner");
            shop.setUser(shopUser);
            shopRepository.save(shop);
        }

        if (!userRepository.existsByEmail("customer@demo.com")) {
            User customerUser = new User();
            customerUser.setEmail("customer@demo.com");
            customerUser.setPassword(passwordEncoder.encode("customer"));

            Role customerRole = this.roleRepository.findByName(Roles.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Role CUSTOMER must exists to initialize users."));
            customerUser.setRole(customerRole);

            userRepository.save(customerUser);

            Customer customer = new Customer();
            customer.setEmail("customer@demo.com");
            customer.setFirstname("Firstname");
            customer.setLastname("Lastname");
            customer.setTin("111111111");
            customer.setUser(customerUser);
            customerRepository.save(customer);
        }
    }
}
