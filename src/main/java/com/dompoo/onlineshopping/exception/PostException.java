package com.dompoo.onlineshopping.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PostException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidatioin(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public abstract String statusCode();
}
