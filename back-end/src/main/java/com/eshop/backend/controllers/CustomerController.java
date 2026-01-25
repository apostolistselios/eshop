package com.eshop.backend.controllers;

import com.eshop.backend.models.Product;
import com.eshop.backend.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer")
@RestController
@RequestMapping(path = "customer")
public class CustomerController {
    @Autowired
    private ProductService productService;

    @GetMapping(path = "products")
    public Page<Product> getProducts(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            Pageable pageable
    ) {
        this.validateRanges(minPrice, maxPrice, minQuantity, maxQuantity);

        return productService.getProducts(
                normalize(type),
                normalize(brand),
                normalize(description),
                minPrice,
                maxPrice,
                minQuantity,
                maxQuantity,
                pageable);
    }

    private void validateRanges(
            Double minPrice,
            Double maxPrice,
            Integer minQuantity,
            Integer maxQuantity
    ) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }

        if (minQuantity != null && maxQuantity != null && minQuantity > maxQuantity) {
            throw new IllegalArgumentException("minQuantity cannot be greater than maxQuantity");
        }
    }

    private String normalize(String value) {
        return (value != null && value.isBlank()) ? null : value;
    }
}
