package com.dompoo.onlineshopping.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String field, String errorMessage) {
        validation.put(field, errorMessage);
    }
}
