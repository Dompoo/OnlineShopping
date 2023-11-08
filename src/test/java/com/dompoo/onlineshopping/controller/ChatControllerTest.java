package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.config.MyMockUser;
import com.dompoo.onlineshopping.domain.*;
import com.dompoo.onlineshopping.repository.ChatMessageRepository;
import com.dompoo.onlineshopping.repository.ChatRoomRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.chat.ChatCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class ChatControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private TestUtil testUtil;

    @AfterEach
    void clean() {
        chatMessageRepository.deleteAll();
        chatRoomRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @MyMockUser
    @DisplayName("대화 시작")
    void startChat() throws Exception {
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

        //expected
        mockMvc.perform(post("/posts/{postId}/chatRoom", savedPost.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("존재하지 않는 글의 대화 시작")
    void startChatFail() throws Exception {
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

        //expected
        mockMvc.perform(post("/posts/{postId}/chatRoom", savedPost.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("채팅 보내기")
    void sendChat() throws Exception {
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

        ChatCreateRequest request = ChatCreateRequest.builder()
                .message("채팅내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts/{roomId}/chat", savedRoom.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("존재하지 않는 채팅방으로 채팅 보내기")
    void sendChatFail() throws Exception {
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

        ChatCreateRequest request = ChatCreateRequest.builder()
                .message("채팅내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts/{roomId}/chat", savedRoom.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 채팅방입니다."))
                .andExpect(jsonPath("$.code").value("404"))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("채팅 조회하기")
    void getChatList() throws Exception {
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
                .message("첫번째 채팅!")
                .chatRoom(savedRoom)
                .user(addUser)
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .message("두번째 채팅!")
                .chatRoom(savedRoom)
                .user(addUser)
                .build());

        //expected
        mockMvc.perform(get("/posts/{roomId}/chat", savedRoom.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].message").value("첫번째 채팅!"))
                .andExpect(jsonPath("$[1].message").value("두번째 채팅!"))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("존재하지 않는 채팅방의 채팅 조회하기")
    void getChatListFail() throws Exception {
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
                .message("첫번째 채팅!")
                .chatRoom(savedRoom)
                .user(addUser)
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .message("두번째 채팅!")
                .chatRoom(savedRoom)
                .user(addUser)
                .build());

        //expected
        mockMvc.perform(get("/posts/{roomId}/chat", savedRoom.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 채팅방입니다."))
                .andExpect(jsonPath("$.code").value("404"))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("채팅방 나가기")
    void deleteChat() throws Exception {
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

        //expected
        mockMvc.perform(delete("/posts/{roomId}/chat", savedRoom.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @MyMockUser
    @DisplayName("존재하지 않는 Id의 채팅방 나가기")
    void deleteChatFail() throws Exception {
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

        //expected
        mockMvc.perform(delete("/posts/{roomId}/chat", savedRoom.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 채팅방입니다."))
                .andExpect(jsonPath("$.code").value("404"))
                .andDo(print());
    }

}