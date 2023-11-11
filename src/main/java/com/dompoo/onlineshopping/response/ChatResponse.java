package com.dompoo.onlineshopping.response;

import com.dompoo.onlineshopping.domain.ChatMessage;
import com.dompoo.onlineshopping.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatResponse {

    private final Long id;
    private final String message;
    private final String username;

    public ChatResponse(ChatMessage message) {
        this.id = message.getId();
        this.message = message.getMessage();
        this.username = message.getUser().getName();
    }

    @Builder
    public ChatResponse(Long id, String message, User user) {
        this.id = id;
        this.message = message;
        this.username = user.getName();
    }
}
