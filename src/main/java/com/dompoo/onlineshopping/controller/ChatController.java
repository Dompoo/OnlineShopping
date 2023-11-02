package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/posts/{postId}/conversation")
    public void startChat(@PathVariable("postId") Long postId) {
        chatService.startChat(postId);
    }
}
