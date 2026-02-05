package com.eshop.backend.controllers;

import com.eshop.backend.dto.product.CreateProductDto;
import com.eshop.backend.dto.product.UpdateProductDto;
import com.eshop.backend.models.Product;
import com.eshop.backend.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Products")
@RestController
@RequestMapping(path = "products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long minQuantity,
            @RequestParam(required = false) Long maxQuantity,
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

    @PreAuthorize("hasRole('SHOP')")
    @GetMapping(path = "manage")
    public Page<Product> getAllProductsForShop(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long minQuantity,
            @RequestParam(required = false) Long maxQuantity,
            Pageable pageable
    ) {
        return productService.getShopProducts(
                type,
                brand,
                description,
                minPrice,
                maxPrice,
                minQuantity,
                maxQuantity,
                pageable);
    }

    @PreAuthorize("hasRole('SHOP')")
    @GetMapping(path = "{id}")
    public Product getProductForShop(@PathVariable Long id) {
        return this.productService.getProductForShop(id);
    }

    @PreAuthorize("hasRole('SHOP')")
    @PostMapping
    public Product createProductForShop(@Valid @RequestBody CreateProductDto data) {
        return this.productService.createProductForShop(data);
    }

    @PreAuthorize("hasRole('SHOP')")
    @PatchMapping(path = "{id}")
    public Product updateProductForShop(@PathVariable Long id, @Valid @RequestBody UpdateProductDto data) {
        return this.productService.updateProductForShop(id, data);
    }

    @PreAuthorize("hasRole('SHOP')")
    @DeleteMapping(path = "{id}")
    public void deleteProductForShop(@PathVariable Long id) {
        this.productService.deleteProductForShop(id);
    }

    private void validateRanges(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long minQuantity,
            Long maxQuantity
    ) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
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
