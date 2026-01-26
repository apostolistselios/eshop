package com.eshop.backend.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String brand, String type) {
        super("Product with brand: " + brand + " and type: " + type + " already exists");
    }
}
