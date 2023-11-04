package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.domain.*;
import com.dompoo.onlineshopping.repository.ChatRepository;
import com.dompoo.onlineshopping.repository.ConversationRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @Autowired private ChatRepository chatRepository;
    @Autowired private ConversationRepository conversationRepository;
    @Autowired private TestUtil testUtil;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
        postRepository.deleteAll();
        productRepository.deleteAll();
        conversationRepository.deleteAll();
    }

    @Test
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
        mockMvc.perform(post("/posts/{postId}/conversation", savedPost.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
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

        Conversation savedConv = conversationRepository.save(Conversation.builder()
                .post(savedPost)
                .build());

        ChatCreateRequest request = ChatCreateRequest.builder()
                .message("채팅내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts/{convId}/chat", savedConv.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
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

        Conversation savedConv = conversationRepository.save(Conversation.builder()
                .post(savedPost)
                .build());

        Chat savedChat1 = chatRepository.save(Chat.builder()
                .message("첫번째 채팅!")
                .conversation(savedConv)
                .build());

        Chat savedChat2 = chatRepository.save(Chat.builder()
                .message("두번째 채팅!")
                .conversation(savedConv)
                .build());

        //when
        mockMvc.perform(get("/posts/{convId}/chat", savedConv.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].message").value("첫번째 채팅!"))
                .andExpect(jsonPath("$[1].message").value("두번째 채팅!"))
                .andDo(print());

        //then

    }

}