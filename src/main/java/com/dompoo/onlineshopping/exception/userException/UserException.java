package com.dompoo.onlineshopping.exception.userException;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class UserException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public abstract String statusCode();
}
