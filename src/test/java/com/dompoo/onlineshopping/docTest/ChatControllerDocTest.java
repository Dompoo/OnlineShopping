package com.dompoo.onlineshopping.docTest;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.onlineshopping.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class ChatControllerDocTest {

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
        this.mockMvc.perform(post("/rooms/{postId}", savedPost.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatRoom-craete",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        )
                ));
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
        mockMvc.perform(post("/chats/{roomId}", savedRoom.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chatMessage-craete",
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        ),
                        requestFields(
                                fieldWithPath("message").description("채팅 메시지")
                        )
                ));
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
        mockMvc.perform(get("/chats/{roomId}", savedRoom.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].message").value("첫번째 채팅!"))
                .andExpect(jsonPath("$[1].message").value("두번째 채팅!"))
                .andDo(print())
                .andDo(document("chatMessage-getList",
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("채팅 ID"),
                                fieldWithPath("[].message").description("채팅 내용"),
                                fieldWithPath("[].username").description("채팅 작성자"),
                                fieldWithPath("[].createdAt").description("채팅 작성 시간"),
                                fieldWithPath("[].displayRight").description("채팅 우측에 표시")
                        )
                ));
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
        mockMvc.perform(delete("/chats/{roomId}", savedRoom.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chatRoom-delete",
                        pathParameters(
                                parameterWithName("roomId").description("채팅방 ID")
                        )
                ));
    }

}