package com.eshop.backend.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String tin) {
        super("Customer with TIN: " + tin + " already exists");
    }
}