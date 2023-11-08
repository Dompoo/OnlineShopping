package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.ChatMessage;
import com.dompoo.onlineshopping.domain.ChatRoom;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.exception.chatException.RoomNotFound;
import com.dompoo.onlineshopping.exception.postException.PostNotFound;
import com.dompoo.onlineshopping.exception.userException.UserNotFound;
import com.dompoo.onlineshopping.repository.ChatMessageRepository;
import com.dompoo.onlineshopping.repository.ChatRoomRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
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

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long startChatRoom(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
                .post(findPost)
                .build());

        return savedChatRoom.getId();
    }

    public void sendMessage(Long userId, Long roomId, @Valid ChatCreateRequest request) {
        User loginUser = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        ChatRoom findRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(RoomNotFound::new);

        chatMessageRepository.save(ChatMessage.builder()
                .message(request.getMessage())
                .chatRoom(findRoom)
                .user(loginUser)
                .build());
    }

    public List<ChatResponse> getMessageList(Long roomId) {
        chatRoomRepository.findById(roomId)
                .orElseThrow(RoomNotFound::new);

        return chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(roomId)
                .stream().map(ChatResponse::new).toList();
    }

    public void deleteChatRoom(Long roomId) {
        ChatRoom findRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(RoomNotFound::new);

        chatRoomRepository.delete(findRoom);
    }
}
