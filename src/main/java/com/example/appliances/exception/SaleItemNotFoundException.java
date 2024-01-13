package com.example.appliances.exception;

public class SaleItemNotFoundException extends RuntimeException {

    public SaleItemNotFoundException(String message) {
        super(message);
    }

    public SaleItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}