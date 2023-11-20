package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.config.UserPrincipal;
import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.dompoo.onlineshopping.response.ChatResponse;
import com.dompoo.onlineshopping.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms/{postId}")
    public Long startRoom(@PathVariable Long postId) {
        return chatService.startChatRoom(postId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/chats/{roomId}")
    public void sendChat(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long roomId, @RequestBody @Valid ChatCreateRequest request) {
        chatService.sendMessage(principal.getUserId(), roomId, request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chats/{roomId}")
    public List<ChatResponse> getChatList(@PathVariable Long roomId) {
        return chatService.getMessageList(roomId);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/chats/{roomId}")
    public void endRoom(@PathVariable Long roomId) {
        chatService.deleteChatRoom(roomId);
    }

}
