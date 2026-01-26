package com.eshop.backend.controllers;

import com.eshop.backend.dto.CreateProductDto;
import com.eshop.backend.dto.UpdateProductDto;
import com.eshop.backend.models.Product;
import com.eshop.backend.services.ProductService;
import com.eshop.backend.services.ShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shop")
@RestController
@RequestMapping(path = "shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

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

    @GetMapping(path = "products/{id}")
    public Product getProductForShop(@PathVariable Long id) {
        return this.productService.getProductForShop(id);
    }

    @PostMapping(path = "products")
    public Product createProductForShop(@Valid @RequestBody CreateProductDto data) {
        return this.productService.createProductForShop(data);
    }

    @PatchMapping(path = "products/{id}")
    public Product updateProductForShop(@PathVariable Long id, @Valid @RequestBody UpdateProductDto data) {
        return this.productService.updateProductForShop(id, data);
    }

    @DeleteMapping(path = "products/{id}")
    public void deleteProductForShop(@PathVariable Long id) {
        this.productService.deleteProductForShop(id);
    }

}
