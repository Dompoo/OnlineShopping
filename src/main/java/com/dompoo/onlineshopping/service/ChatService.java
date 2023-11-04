package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Chat;
import com.dompoo.onlineshopping.domain.Conversation;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.exception.ConvNotFound;
import com.dompoo.onlineshopping.exception.postException.PostNotFound;
import com.dompoo.onlineshopping.repository.ChatRepository;
import com.dompoo.onlineshopping.repository.ConversationRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.dompoo.onlineshopping.response.ChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ConversationRepository conversationRepository;
    private final PostRepository postRepository;

    public Long startChat(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        Conversation savedConversation = conversationRepository.save(Conversation.builder()
                .post(findPost)
                .build());

        return savedConversation.getId();
    }

    public void sendChat(Long convId, @Valid ChatCreateRequest request) {
        Conversation findConv = conversationRepository.findById(convId)
                .orElseThrow(ConvNotFound::new);

        chatRepository.save(Chat.builder()
                .message(request.getMessage())
                .conversation(findConv)
                .build());
    }

    public List<ChatResponse> getChatList(Long convId) {
        return chatRepository.findByConversation_IdOrderByCreatedAtAsc(convId)
                .stream().map(ChatResponse::new).toList();
    }
}
