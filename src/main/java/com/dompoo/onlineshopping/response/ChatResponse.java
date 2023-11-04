package com.dompoo.onlineshopping.response;

import com.dompoo.onlineshopping.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatResponse {

    private final Long id;
    private final String message;

    public ChatResponse(ChatMessage message) {
        this.id = message.getId();
        this.message = message.getMessage();
    }

    @Builder
    public ChatResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}
