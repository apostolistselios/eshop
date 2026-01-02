package com.eshop.backend.config;

import com.eshop.backend.constants.Roles;
import com.eshop.backend.models.*;
import com.eshop.backend.repositories.*;
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

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initializeRoles();
        this.initializeUsers();
        this.initializeProducts();
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

    private void initializeProducts() {
        Shop shop = this.shopRepository.findByTin("999999999")
                .orElseThrow(() -> new RuntimeException("Invalid demo shop"));

        Product laptop = new Product();
        laptop.setType("Laptop");
        laptop.setBrand("Lenovo");
        laptop.setDescription("A great laptop");
        laptop.setPrice(500);
        laptop.setQuantity(3);
        laptop.setShop(shop);
        this.productRepository.save(laptop);

        Product pc = new Product();
        pc.setType("PC");
        pc.setBrand("Dell");
        pc.setDescription("A great PC");
        pc.setPrice(700);
        pc.setQuantity(5);
        pc.setShop(shop);
        this.productRepository.save(pc);

        Product monitor = new Product();
        monitor.setType("Monitor");
        monitor.setBrand("Dell");
        monitor.setDescription("A great monitor");
        monitor.setPrice(200);
        monitor.setQuantity(1);
        monitor.setShop(shop);
        this.productRepository.save(monitor);

        Product keyboard = new Product();
        keyboard.setType("Keyboard");
        keyboard.setBrand("Microsoft");
        keyboard.setDescription("A great keyboard");
        keyboard.setPrice(50);
        keyboard.setQuantity(0);
        keyboard.setShop(shop);
        this.productRepository.save(keyboard);

        Product mouse = new Product();
        mouse.setType("Mouse");
        mouse.setBrand("Microsoft");
        mouse.setDescription("A great mouse");
        mouse.setPrice(20);
        mouse.setQuantity(2);
        mouse.setShop(shop);
        this.productRepository.save(mouse);
    }
}
