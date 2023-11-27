package com.dompoo.onlineshopping.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponse {

    private final Long id;
    private final String message;
    private final String username;
    private final LocalDateTime createdAt;
    private final Boolean displayRight;

    @Builder
    public ChatResponse(Long id, String message, String username, Boolean isLoginUser) {
        this.id = id;
        this.message = message;
        this.username = username;
        this.createdAt = LocalDateTime.now();
        this.displayRight = isLoginUser;
    }
}
