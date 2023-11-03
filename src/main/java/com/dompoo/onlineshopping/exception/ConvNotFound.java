package com.dompoo.onlineshopping.exception;

public class ConvNotFound extends ChatException {

    private static final String MESSAGE = "존재하지 않는 대화입니다.";
    private static final String STATUS_CODE = "404";

    public ConvNotFound() {
        super(MESSAGE);
    }

    public ConvNotFound(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
