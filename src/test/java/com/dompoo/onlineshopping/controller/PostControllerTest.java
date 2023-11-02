package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.config.MyMockUser;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.post.PostCreateRequest;
import com.dompoo.onlineshopping.request.post.PostEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class PostControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtil testUtil;
    @Autowired private EntityManager em;

    @AfterEach
    void clean() {
        em.clear();
        productRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 작성")
    void post1() throws Exception {
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
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 조회")
    void get1() throws Exception {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .user(addUser)
                .product(savedProduct)
                .build());

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 1개 조회")
    void get2() throws Exception {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .user(addUser)
                .product(savedProduct)
                .build());

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getList1() throws Exception {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .user(addUser)
                        .product(savedProduct)
                        .build()
                )
                .toList();
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andExpect(jsonPath("$[4].title").value("제목 26"))
                .andExpect(jsonPath("$[4].content").value("내용 26"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회시 페이지를 0으로 요청해도 첫 페이지를 가져온다.")
    void getList2() throws Exception {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilderPlain()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .user(addUser)
                        .product(savedProduct)
                        .build()
                )
                .toList();
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andExpect(jsonPath("$[4].title").value("제목 26"))
                .andExpect(jsonPath("$[4].content").value("내용 26"))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 제목 수정, DB값 변경")
    void patch1() throws Exception {
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .user(findUser)
                .product(savedProduct)
                .build());

        PostEditRequest postEditRequest = PostEditRequest.builder()
                .title("새로운글제목입니다.")
                .build();

        //when
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEditRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다."));
        assertEquals("새로운글제목입니다.", findPost.getTitle());
        assertEquals("글내용입니다.", findPost.getContent());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 내용 수정, DB값 변경")
    void patch2() throws Exception {
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .user(findUser)
                .product(savedProduct)
                .build());

        PostEditRequest postEditRequest = PostEditRequest.builder()
                .content("새로운글내용입니다.")
                .build();

        //when
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEditRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다."));
        assertEquals("글제목입니다.", findPost.getTitle());
        assertEquals("새로운글내용입니다.", findPost.getContent());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("존재하지 않는 글 수정")
    void patch4() throws Exception {
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .user(findUser)
                .product(savedProduct)
                .build());

        PostEditRequest postEditRequest = PostEditRequest.builder()
                .title("새로운글제목입니다.")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", post.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEditRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 삭제, DB값 변경")
    void delete1() throws Exception {
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .user(findUser)
                .product(savedProduct)
                .build());

        //when
        mockMvc.perform(delete("/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        assertEquals(0L, postRepository.count());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("존재하지 않는 글 삭제")
    void delete2() throws Exception {
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        Post post = postRepository.save(testUtil.newPostBuilder()
                .user(findUser)
                .product(savedProduct)
                .build());

        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."))
                .andDo(print());
    }
}