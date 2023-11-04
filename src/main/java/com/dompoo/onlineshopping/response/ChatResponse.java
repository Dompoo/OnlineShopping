package com.dompoo.onlineshopping.response;

import com.dompoo.onlineshopping.domain.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatResponse {

    private final Long id;
    private final String message;

    public ChatResponse(Chat chat) {
        this.id = chat.getId();
        this.message = chat.getMessage();
    }

    @Builder
    public ChatResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}
