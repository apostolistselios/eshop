package com.eshop.backend.services;

import com.eshop.backend.models.Product;
import com.eshop.backend.models.Shop;
import com.eshop.backend.models.User;
import com.eshop.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    public Page<Product> getProducts(
            String type,
            String brand,
            String description,
            Double minPrice,
            Double maxPrice,
            Integer minQuantity,
            Integer maxQuantity,
            Pageable pageable
    ) {
        return this.productRepository.findByFilters(
                type,
                brand,
                description,
                minPrice,
                maxPrice,
                minQuantity,
                maxQuantity,
                pageable);
    }

    public Page<Product> getShopProducts(
            String type,
            String brand,
            String description,
            Double minPrice,
            Double maxPrice,
            Integer minQuantity,
            Integer maxQuantity,
            Pageable pageable
    ) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = this.userService.findByEmail(email);
        Shop shop = this.shopService.findByUser(user);

        return this.productRepository.findShopProductsByFilters(
                shop.getId(),
                type,
                brand,
                description,
                minPrice,
                maxPrice,
                minQuantity,
                maxQuantity,
                pageable);
    }
}
