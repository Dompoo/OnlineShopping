package com.dompoo.onlineshopping.exception;

public abstract class PostException extends RuntimeException {

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String statusCode();
}
