package com.dompoo.onlineshopping.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ChatCreateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String message;

    @Builder
    public ChatCreateRequest(String message) {
        this.message = message;
    }
}
