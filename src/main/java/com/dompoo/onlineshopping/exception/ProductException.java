package com.dompoo.onlineshopping.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ProductException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public abstract String statusCode();
}
