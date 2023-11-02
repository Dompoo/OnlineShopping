package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.repository.ChatRepository;
import com.dompoo.onlineshopping.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ConversationRepository conversationRepository;
}
