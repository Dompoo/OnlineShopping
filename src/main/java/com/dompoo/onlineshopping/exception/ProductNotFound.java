package com.dompoo.onlineshopping.exception;

public class ProductNotFound  extends ProductException {

    private static final String MESSAGE = "존재하지 않는 상품입니다.";
    private static final String STATUS_CODE = "404";

    public ProductNotFound() {
        super(MESSAGE);
    }

    public ProductNotFound(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
