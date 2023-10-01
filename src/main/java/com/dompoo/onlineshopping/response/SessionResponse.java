package com.dompoo.onlineshopping.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionResponse {

    private final String accessToken;

    @Builder
    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
