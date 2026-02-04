package com.eshop.backend.exceptions;

import com.eshop.backend.models.Product;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(Product product) {
        super("Not enough stock for product: " + product.getType() + " " + product.getBrand());
    }
}
