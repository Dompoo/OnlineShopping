package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.dompoo.onlineshopping.response.ChatResponse;
import com.dompoo.onlineshopping.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/posts/{postId}/chatRoom")
    public Long startChat(@PathVariable Long postId) {
        return chatService.startChatRoom(postId);
    }

    @PostMapping("/posts/{roomId}/chat")
    public void sendChat(@PathVariable Long roomId, @RequestBody @Valid ChatCreateRequest request) {
        chatService.sendMessage(roomId, request);
    }

    @GetMapping("/posts/{roomId}/chat")
    public List<ChatResponse> getChatList(@PathVariable Long roomId) {
        return chatService.getMessageList(roomId);
    }
}
