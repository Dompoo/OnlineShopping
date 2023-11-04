package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.domain.*;
import com.dompoo.onlineshopping.repository.ChatRepository;
import com.dompoo.onlineshopping.repository.ConversationRepository;
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
    @Autowired private ChatRepository chatRepository;
    @Autowired private ConversationRepository conversationRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtil testUtil;

    @BeforeEach
    void clean() {
        chatRepository.deleteAll();
        conversationRepository.deleteAll();
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
        Long convId = chatService.startChat(savedPost.getId());

        //then
        assertEquals(1L, conversationRepository.count());
        assertEquals(convId, conversationRepository.findAll().get(0).getId());
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

        Conversation savedConv = conversationRepository.save(Conversation.builder()
                .post(savedPost)
                .build());

        //when
        chatService.sendChat(savedConv.getId(), ChatCreateRequest.builder()
                .message("첫번째 채팅")
                .build());
        chatService.sendChat(savedConv.getId(), ChatCreateRequest.builder()
                .message("두번째 채팅")
                .build());

        //then
        assertEquals(2L, chatRepository.count());
        List<Chat> findChats = chatRepository.findByConversation_IdOrderByCreatedAtAsc(savedConv.getId());
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

        Conversation savedConv = conversationRepository.save(Conversation.builder()
                .post(savedPost)
                .build());

        Chat savedChat1 = chatRepository.save(Chat.builder()
                .conversation(savedConv)
                .message("첫번째 채팅")
                .build());

        Chat savedChat2 = chatRepository.save(Chat.builder()
                .conversation(savedConv)
                .message("두번째 채팅")
                .build());

        //when
        List<ChatResponse> findChats = chatService.getChatList(savedConv.getId());

        //then
        assertEquals(2L, findChats.size());
        assertEquals("첫번째 채팅", findChats.get(0).getMessage());
        assertEquals("두번째 채팅", findChats.get(1).getMessage());
    }
}