package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.dompoo.onlineshopping.response.ChatResponse;
import com.dompoo.onlineshopping.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/{postId}/chatRoom")
    public Long startChat(@PathVariable Long postId) {
        return chatService.startChatRoom(postId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/{roomId}/chat")
    public void sendChat(@PathVariable Long roomId, @RequestBody @Valid ChatCreateRequest request) {
        chatService.sendMessage(roomId, request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/{roomId}/chat")
    public List<ChatResponse> getChatList(@PathVariable Long roomId) {
        return chatService.getMessageList(roomId);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/posts/{roomId}/chat")
    public void deleteChat(@PathVariable Long roomId) {
        chatService.deleteChatRoom(roomId);
    }

}
