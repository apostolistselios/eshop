package com.eshop.backend.config;

import com.eshop.backend.constants.Roles;
import com.eshop.backend.models.Role;
import com.eshop.backend.models.User;
import com.eshop.backend.repositories.RoleRepository;
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
        if (!userRepository.existsByEmail("shop@test.com")) {
            User shopUser = new User();
            shopUser.setEmail("shop@test.com");
            shopUser.setPassword(passwordEncoder.encode("shop"));

            Optional<Role> shopRole = this.roleRepository.findByName(Roles.SHOP);
            if (shopRole.isPresent()) {
                shopUser.setRole(shopRole.get());
            }

            userRepository.save(shopUser);
        }

        if (!userRepository.existsByEmail("customer@test.com")) {
            User customerUser = new User();
            customerUser.setEmail("customer@test.com");
            customerUser.setPassword(passwordEncoder.encode("customer"));

            Optional<Role> customerRole = this.roleRepository.findByName(Roles.CUSTOMER);
            if (customerRole.isPresent()) {
                customerUser.setRole(customerRole.get());
            }

            userRepository.save(customerUser);
        }
    }
}
