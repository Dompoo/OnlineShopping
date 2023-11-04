package com.dompoo.onlineshopping.exception.chatException;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ChatException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public ChatException(String message) {
        super(message);
    }

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public abstract String statusCode();
}
