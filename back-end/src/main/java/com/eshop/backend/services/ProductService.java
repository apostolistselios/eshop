package com.eshop.backend.services;

import com.eshop.backend.models.Product;
import com.eshop.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

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
        return productRepository.findByFilters(
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
