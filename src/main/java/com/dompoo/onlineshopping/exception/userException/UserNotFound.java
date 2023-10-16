package com.dompoo.onlineshopping.exception.userException;

public class UserNotFound extends UserException {

    private static final String MESSAGE = "존재하지 않는 유저입니다.";
    private static final String STATUS_CODE = "404";

    public UserNotFound() {
        super(MESSAGE);
    }

    public UserNotFound(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
