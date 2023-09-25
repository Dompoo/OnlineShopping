package com.dompoo.onlineshopping.exception;

public abstract class PageException extends RuntimeException {

    public PageException(String message) {
        super(message);
    }

    public PageException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String statusCode();
}
