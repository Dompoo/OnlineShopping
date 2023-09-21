package com.dompoo.onlineshopping.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code" : "400"
 *     "message" : "~~"
 *     "validation" : {
 *         "title" : "~~"
 *     }
 * }
 */

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String field, String errorMessage) {
        validation.put(field, errorMessage);
    }
}
