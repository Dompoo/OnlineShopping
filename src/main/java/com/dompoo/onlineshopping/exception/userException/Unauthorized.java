package com.dompoo.onlineshopping.exception.userException;

/**
 * status : 401
 */
public class Unauthorized extends UserException {

    private static final String MESSAGE = "인증이 필요합니다.";
    private static final String STATUS_CODE = "401";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
