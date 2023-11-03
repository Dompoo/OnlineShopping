package com.dompoo.onlineshopping.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatCreateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String message;

    @Builder
    public ChatCreateRequest(String message) {
        this.message = message;
    }
}
