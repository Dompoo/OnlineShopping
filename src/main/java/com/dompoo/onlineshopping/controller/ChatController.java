package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
}
