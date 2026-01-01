package com.eshop.backend.exceptions;

public class ShopAlreadyExistsException extends RuntimeException {
    public ShopAlreadyExistsException(String tin) {
        super("Shop with TIN: " + tin + " already exists");
    }
}