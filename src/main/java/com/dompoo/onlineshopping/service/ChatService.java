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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * <pre>
     * 설명 : 채팅방을 개설합니다.
     * 채팅방은 하나의 글에서 첫 대화를 시작할 때 한번만 만들면 됩니다.
     *
     * 동작 : 채팅방을 개설할 postId와 로그인한 사용자 userId가 주어지면,
     * 해당 글과 사용자를 찾고, 이미 개설된 채팅방이 있는지 확인합니다.
     * 개설된 채팅방이 있다면 이 채팅방의 Id를 리턴하고,
     * 개설된 채팅방이 없다면 ChatRoom 객체를 만들어 저장하고 Id를 리턴합니다.
     */
    public Long startChatRoom(Long userId, Long postId) {
        User loginUser = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        Optional<ChatRoom> findRoom = chatRoomRepository.findByUserAndPost(loginUser, findPost);
        if (findRoom.isPresent()) {
            return findRoom.get().getId();
        } else {
            ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
                    .post(findPost)
                    .build());

            return savedChatRoom.getId();
        }
    }

    /**
     * <pre>
     * 설명 : 채팅을 보냅니다.
     *
     * 동작 : 채팅을 보낸 userId, 채팅방의 roomId, 채팅메시지가 주어집니다.
     * 이를 바탕으로 유저와 채팅방을 찾고,
     * ChatMessage 객체를 만들어 저장합니다.
     */
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

    /**
     * <pre>
     * 설명 : 채팅리스트를 조회합니다.
     * 채팅리스트는 일반적으로 채팅방에 들어가면 나오는 정보이며,
     * 리턴 객체에 username, message 등이 들어갑니다.
     * 또한 현재 로그인한 유저의 Id와 메시지를 작성한 유저의 Id를 비교하여 isLoginUser 값을 넣어줍니다.
     *
     * 동작 : 채팅방의 roomId가 주어지면, 먼저 채팅방이 있는지 확인하고,
     * 해당 채팅방의 ChatMessage 객체들을 생성날짜 순으로 조회합니다.
     * 그 후 List로 매핑하여 리턴합니다.
     */
    public List<ChatResponse> getMessageList(Long userId, Long roomId) {
        chatRoomRepository.findById(roomId)
                .orElseThrow(RoomNotFound::new);

        return chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(roomId)
                .stream().map(chat ->
                        ChatResponse.builder()
                                .id(chat.getId())
                                .message(chat.getMessage())
                                .username(chat.getUser().getName())
                                .isLoginUser(chat.getUser().getId().equals(userId))
                                .build())
                .toList();
    }

    /**
     * <pre>
     * 설명 : 채팅방을 삭제합니다.
     * 채팅방을 삭제하면 해당 채팅방의  ChatMessage 객체들도 같이 삭제됩니다.
     *
     * 동작 : 채팅방의 roomId가 주어지면, 먼저 채팅방을 찾고,
     * 채팅방이 있다면 그 채팅방을 삭제합니다.
     */
    public void deleteChatRoom(Long roomId) {
        ChatRoom findRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(RoomNotFound::new);

        chatRoomRepository.delete(findRoom);
    }
}
