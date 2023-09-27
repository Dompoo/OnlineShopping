package com.dompoo.onlineshopping.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> postException(PostException e) {
        ErrorResponse errorBody = ErrorResponse.builder()
                .code(e.statusCode())
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(Integer.parseInt(e.statusCode()))
                .body(errorBody);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> productException(ProductException e) {
        ErrorResponse errorBody = ErrorResponse.builder()
                .code(e.statusCode())
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(Integer.parseInt(e.statusCode()))
                .body(errorBody);
    }
}
