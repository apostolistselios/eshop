package com.eshop.backend.config;

import com.eshop.backend.constants.Roles;
import com.eshop.backend.models.*;
import com.eshop.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
        this.initializeUsersAndShops();
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

    private void initializeUsersAndShops() {
        createShopIfMissing("shop1@demo.com", "shop1", "999999991", "Shop 1", "Shop 1 Owner");
        createShopIfMissing("shop2@demo.com", "shop2", "999999992", "Shop 2", "Shop 2 Owner");
        createShopIfMissing("shop3@demo.com", "shop3", "999999993", "Shop 3", "Shop 3 Owner");

        // --- Create 1 customer (if not exist) ---
        if (!userRepository.existsByEmail("customer@demo.com")) {
            User customerUser = new User();
            customerUser.setEmail("customer@demo.com");
            customerUser.setPassword(passwordEncoder.encode("customer"));

            Role customerRole = this.roleRepository.findByName(Roles.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Role CUSTOMER must exist to initialize users."));
            customerUser.setRole(customerRole);

            this.userRepository.save(customerUser);

            Customer customer = new Customer();
            customer.setEmail("customer@demo.com");
            customer.setFirstname("Customer First Name");
            customer.setLastname("Customer Last Name");
            customer.setTin("111111111");
            customer.setUser(customerUser);

            this.customerRepository.save(customer);
        }
    }

    private void createShopIfMissing(String email, String rawPassword, String tin, String brandName, String owner) {
        if (this.userRepository.existsByEmail(email)) {
            return;
        }

        User shopUser = new User();
        shopUser.setEmail(email);
        shopUser.setPassword(passwordEncoder.encode(rawPassword));

        Role shopRole = this.roleRepository.findByName(Roles.SHOP)
                .orElseThrow(() -> new RuntimeException("Role SHOP must exist to initialize users."));
        shopUser.setRole(shopRole);

        this.userRepository.save(shopUser);

        Shop shop = new Shop();
        shop.setTin(tin);
        shop.setBrandName(brandName);
        shop.setOwner(owner);
        shop.setUser(shopUser);

        this.shopRepository.save(shop);
    }

    private void initializeProducts() {
        if (this.productRepository.count() > 0) {
            return;
        }

        List<Shop> shops = List.of(
                this.shopRepository.findByTin("999999991").orElseThrow(() -> new RuntimeException("Invalid demo shop1")),
                this.shopRepository.findByTin("999999992").orElseThrow(() -> new RuntimeException("Invalid demo shop2")),
                this.shopRepository.findByTin("999999993").orElseThrow(() -> new RuntimeException("Invalid demo shop3"))
        );

        // Simple pools to generate products
        List<String> types = List.of("Laptop", "PC", "Monitor", "Keyboard", "Mouse", "Headphones", "Webcam", "Printer", "Router", "SSD");
        Map<String, List<String>> brandsByType = new HashMap<>();
        brandsByType.put("Laptop", List.of("Lenovo", "Dell", "HP", "Asus", "Acer"));
        brandsByType.put("PC", List.of("Dell", "HP", "Lenovo", "Asus"));
        brandsByType.put("Monitor", List.of("Dell", "LG", "Samsung", "AOC"));
        brandsByType.put("Keyboard", List.of("Logitech", "Microsoft", "Razer", "Corsair"));
        brandsByType.put("Mouse", List.of("Logitech", "Microsoft", "Razer", "SteelSeries"));
        brandsByType.put("Headphones", List.of("Sony", "JBL", "Logitech", "Razer"));
        brandsByType.put("Webcam", List.of("Logitech", "Microsoft", "Razer"));
        brandsByType.put("Printer", List.of("HP", "Canon", "Epson"));
        brandsByType.put("Router", List.of("TP-Link", "Asus", "Netgear"));
        brandsByType.put("SSD", List.of("Samsung", "Kingston", "Crucial", "WD"));

        List<String> adjectives = List.of("Great", "Solid", "Premium", "Budget", "Compact", "High-performance", "Silent", "Wireless");
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            String type = types.get(rnd.nextInt(types.size()));
            List<String> brands = brandsByType.getOrDefault(type, List.of("Generic"));
            String brand = brands.get(rnd.nextInt(brands.size()));

            Shop shop = shops.get(rnd.nextInt(shops.size()));

            Product p = new Product();
            p.setType(type);
            p.setBrand(brand);

            String adj = adjectives.get(rnd.nextInt(adjectives.size()));
            p.setDescription(adj + " " + type + " by " + brand);

            // Price: basic ranges per type (adjust to your entity type)
            BigDecimal price = generatePrice(type, rnd);
            p.setPrice(price);

            long quantity = rnd.nextLong(0, 51); // 0..50
            p.setQuantity(quantity);

            p.setShop(shop);

            products.add(p);
        }

        this.productRepository.saveAll(products);
    }

    private BigDecimal generatePrice(String type, ThreadLocalRandom rnd) {

        long minCents;
        long maxCents;

        switch (type) {
            case "Laptop" -> {
                minCents = 40_000;
                maxCents = 160_000;
            }
            case "PC" -> {
                minCents = 50_000;
                maxCents = 220_000;
            }
            case "Monitor" -> {
                minCents = 12_000;
                maxCents = 70_000;
            }
            case "Keyboard" -> {
                minCents = 1_500;
                maxCents = 20_000;
            }
            case "Mouse" -> {
                minCents = 1_000;
                maxCents = 15_000;
            }
            case "Headphones" -> {
                minCents = 2_000;
                maxCents = 35_000;
            }
            case "Webcam" -> {
                minCents = 2_500;
                maxCents = 25_000;
            }
            case "Printer" -> {
                minCents = 6_000;
                maxCents = 60_000;
            }
            case "Router" -> {
                minCents = 2_000;
                maxCents = 30_000;
            }
            case "SSD" -> {
                minCents = 3_000;
                maxCents = 40_000;
            }
            default -> {
                minCents = 2_000;
                maxCents = 30_000;
            }
        }

        long cents = rnd.nextLong(minCents, maxCents + 1);

        // exactly 2 decimal places
        return BigDecimal.valueOf(cents, 2);
    }
}
