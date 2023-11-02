package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Conversation;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.exception.postException.PostNotFound;
import com.dompoo.onlineshopping.repository.ChatRepository;
import com.dompoo.onlineshopping.repository.ConversationRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
