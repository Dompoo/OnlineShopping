package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.dompoo.onlineshopping.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/posts/{postId}/conversation")
    public Long startChat(@PathVariable Long postId) {
        return chatService.startChat(postId);
    }

    @PostMapping("/posts/{convId}/chat")
    public void sendChat(@PathVariable Long convId, @RequestBody @Valid ChatCreateRequest request) {
        chatService.sendChat(convId, request);
    }
}
