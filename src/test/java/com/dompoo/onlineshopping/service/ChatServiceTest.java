package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.domain.*;
import com.dompoo.onlineshopping.repository.ChatMessageRepository;
import com.dompoo.onlineshopping.repository.ChatRoomRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.dompoo.onlineshopping.response.ChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ChatServiceTest {

    @Autowired private ChatService chatService;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtil testUtil;

    @BeforeEach
    void clean() {
        chatMessageRepository.deleteAll();
        chatRoomRepository.deleteAll();
        productRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("채팅방 생성")
    void startChat() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(addUser)
                .product(savedProduct)
                .build());

        //when
        Long convId = chatService.startChatRoom(savedPost.getId());

        //then
        assertEquals(1L, chatRoomRepository.count());
        assertEquals(convId, chatRoomRepository.findAll().get(0).getId());
    }

    @Test
    @DisplayName("채팅 보내기")
    void sendChat() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(addUser)
                .product(savedProduct)
                .build());

        ChatRoom savedRoom = chatRoomRepository.save(ChatRoom.builder()
                .post(savedPost)
                .build());

        //when
        chatService.sendMessage(savedRoom.getId(), ChatCreateRequest.builder()
                .message("첫번째 채팅")
                .build());
        chatService.sendMessage(savedRoom.getId(), ChatCreateRequest.builder()
                .message("두번째 채팅")
                .build());

        //then
        assertEquals(2L, chatMessageRepository.count());
        List<ChatMessage> findChats = chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(savedRoom.getId());
        assertEquals("첫번째 채팅", findChats.get(0).getMessage());
        assertEquals("두번째 채팅", findChats.get(1).getMessage());

    }

    @Test
    @DisplayName("채팅리스트 조회")
    void getChatList() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(addUser)
                .product(savedProduct)
                .build());

        ChatRoom savedRoom = chatRoomRepository.save(ChatRoom.builder()
                .post(savedPost)
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(savedRoom)
                .message("첫번째 채팅")
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(savedRoom)
                .message("두번째 채팅")
                .build());

        //when
        List<ChatResponse> findChats = chatService.getMessageList(savedRoom.getId());

        //then
        assertEquals(2L, findChats.size());
        assertEquals("첫번째 채팅", findChats.get(0).getMessage());
        assertEquals("두번째 채팅", findChats.get(1).getMessage());
    }

    @Test
    @DisplayName("채팅방 나가기")
    void deleteChatRoom() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(addUser)
                .product(savedProduct)
                .build());

        ChatRoom savedRoom = chatRoomRepository.save(ChatRoom.builder()
                .post(savedPost)
                .build());

        //when
        chatService.deleteChatRoom(savedRoom.getId());

        //then
        assertEquals(0L, chatRoomRepository.count());
    }
}