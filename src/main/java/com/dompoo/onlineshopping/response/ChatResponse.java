package com.dompoo.onlineshopping.response;

import com.dompoo.onlineshopping.domain.ChatMessage;
import com.dompoo.onlineshopping.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponse {

    private final Long id;
    private final String message;
    private final String username;
    private final LocalDateTime createdAt;

    public ChatResponse(ChatMessage message) {
        this.id = message.getId();
        this.message = message.getMessage();
        this.username = message.getUser().getName();
        this.createdAt = message.getCreatedAt();
    }

    @Builder
    public ChatResponse(Long id, String message, User user) {
        this.id = id;
        this.message = message;
        this.username = user.getName();
        this.createdAt = LocalDateTime.now();
    }
}
