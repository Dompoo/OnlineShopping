package com.dompoo.onlineshopping.exception;

public class InvalidSigninInfo extends PostException {

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";
    private static final String STATUS_CODE = "400";

    public InvalidSigninInfo() {
        super(MESSAGE);
    }

    public InvalidSigninInfo(String fieldName, String message) {
        super(MESSAGE);
        addValidatioin(fieldName, message);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
