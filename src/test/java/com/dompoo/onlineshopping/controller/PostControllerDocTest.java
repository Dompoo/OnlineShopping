package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.config.MyMockUser;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.request.PostEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.onlineshopping.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private PostRepository postRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtil testUtil;
    @Autowired private EntityManager em;

    @AfterEach
    void clean() {
        em.clear();
        postRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 등록")
    void test1() throws Exception {
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        PostCreateRequest request = PostCreateRequest.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .productId(savedProduct.getId())
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        this.mockMvc.perform(post("/posts")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-craete",
                        requestFields(
                                fieldWithPath("title").description("게시글 제목").optional(),
                                fieldWithPath("content").description("게시글 내용").optional(),
                                fieldWithPath("productId").description("상품 ID")
                        )
                ));
    }

    @Test
    @DisplayName("글 단건 조회")
    void test2() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(savedUser)
                .product(savedProduct)
                .build());

        //expected
        this.mockMvc.perform(get("/posts/{postId}", savedPost.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-getOne",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 다건 조회")
    void test3() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .user(savedUser)
                        .product(savedProduct)
                        .build()
                )
                .toList();
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=5")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-getList",
                        responseFields(
                                fieldWithPath("[].id").description("게시글 ID"),
                                fieldWithPath("[].title").description("게시글 제목"),
                                fieldWithPath("[].content").description("게시글 내용")
                        )
                ));
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 수정")
    void tets4() throws Exception{
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(findUser)
                .product(savedProduct)
                .build());

        PostEditRequest postEditRequest = PostEditRequest.builder()
                .title("새로운글제목입니다.")
                .content("새로운글내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(postEditRequest);

        //expected
        mockMvc.perform(patch("/posts/{postId}", savedPost.getId())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-update",
                        pathParameters(
                                parameterWithName("postId").description("업데이트할 게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("업데이트할 게시글 제목"),
                                fieldWithPath("content").description("업데이트할 게시글 내용")
                        )
                ));
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 삭제")
    void test5() throws Exception{
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post savedPost = postRepository.save(testUtil.newPostBuilder()
                .user(findUser)
                .product(savedProduct)
                .build());

        //expected
        mockMvc.perform(delete("/posts/{postId}", savedPost.getId())
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("삭제할 게시글 ID")
                        )
                ));

    }
}
