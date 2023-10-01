package com.dompoo.onlineshopping.exception.userException;

public class AlreadyExistsEmailException extends UserException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";
    private static final String STATUS_CODE = "404";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    public AlreadyExistsEmailException(String filedName, String message) {
        super(MESSAGE);
        addValidation(filedName, message);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
