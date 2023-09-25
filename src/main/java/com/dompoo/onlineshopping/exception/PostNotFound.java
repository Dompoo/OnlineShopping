package com.dompoo.onlineshopping.exception;

public class PostNotFound extends PostException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";
    private static final String STATUS_CODE = "404";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
