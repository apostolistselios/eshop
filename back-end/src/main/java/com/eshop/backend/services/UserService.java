package com.eshop.backend.services;

import com.eshop.backend.constants.Roles;
import com.eshop.backend.exceptions.NotFoundException;
import com.eshop.backend.exceptions.UserAlreadyExistsException;
import com.eshop.backend.models.Role;
import com.eshop.backend.models.User;
import com.eshop.backend.repositories.RoleRepository;
import com.eshop.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User createShopUser(String email, String password) {
        this.assertEmailDoesNotExist(email);

        Role shopRole = this.roleRepository.findByName(Roles.SHOP)
                .orElseThrow(() -> new RuntimeException("Role SHOP not found"));
        User shopUser = new User();
        shopUser.setEmail(email);
        shopUser.setPassword(this.passwordEncoder.encode(password));
        shopUser.setRole(shopRole);

        User savedShopUser = this.userRepository.save(shopUser);

        return savedShopUser;
    }

    public User createCustomerUser(String email, String password) {
        this.assertEmailDoesNotExist(email);

        Role customerRole = this.roleRepository.findByName(Roles.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));
        User customerUser = new User();
        customerUser.setEmail(email);
        customerUser.setPassword(this.passwordEncoder.encode(password));
        customerUser.setRole(customerRole);

        User savedCustomerUser = this.userRepository.save(customerUser);

        return savedCustomerUser;
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = this.findByEmail(email);

        return user;
    }

    private void assertEmailDoesNotExist(String email) {
        if (this.userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
    }
}
