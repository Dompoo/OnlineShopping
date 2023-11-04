package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.ChatMessage;
import com.dompoo.onlineshopping.domain.ChatRoom;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.exception.ConvNotFound;
import com.dompoo.onlineshopping.exception.postException.PostNotFound;
import com.dompoo.onlineshopping.repository.ChatMessageRepository;
import com.dompoo.onlineshopping.repository.ChatRoomRepository;
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

    private final ChatMessageRepository chatRepository;
    private final ChatRoomRepository conversationRepository;
    private final PostRepository postRepository;

    public Long startChatRoom(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        ChatRoom savedChatRoom = conversationRepository.save(ChatRoom.builder()
                .post(findPost)
                .build());

        return savedChatRoom.getId();
    }

    public void sendMessage(Long convId, @Valid ChatCreateRequest request) {
        ChatRoom findConv = conversationRepository.findById(convId)
                .orElseThrow(ConvNotFound::new);

        chatRepository.save(ChatMessage.builder()
                .message(request.getMessage())
                .chatRoom(findConv)
                .build());
    }

    public List<ChatResponse> getMessageList(Long roomId) {
        return chatRepository.findByChatRoom_IdOrderByCreatedAtAsc(roomId)
                .stream().map(ChatResponse::new).toList();
    }
}
