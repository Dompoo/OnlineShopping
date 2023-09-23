package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.repository.PostRepository;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void write() {
        //given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();

        //when
        postService.write(request);

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("글제목입니다.", post.getTitle());
        assertEquals("글내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void get() {
        //given
        Post post = Post.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();
        Post savedPost = postRepository.save(post);

        //when
        PostResponse findPost = postService.get(savedPost.getId());

        //then
        assertNotNull(findPost);
        assertEquals("글제목입니다.", findPost.getTitle());
        assertEquals("글내용입니다.", findPost.getContent());
    }

    @Test
    @DisplayName("글 1개 조회시 글제목은 최대 10글자이다.")
    void get2() {
        //given
        Post post = Post.builder()
                .title("12345678901234567890")
                .content("글내용입니다.")
                .build();
        Post savedPost = postRepository.save(post);

        //when
        PostResponse findPost = postService.get(savedPost.getId());

        //then
        assertNotNull(findPost);
        assertEquals("1234567890", findPost.getTitle());
        assertEquals("글내용입니다.", findPost.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getList() {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                            .title("제목 " + i)
                            .content("내용 " + i)
                            .build()
                )
                .toList();
        postRepository.saveAll(requestPosts);

        PageRequest pageRequest = PageRequest.of(0, 5, DESC, "id");


        //when
        List<PostResponse> findPosts = postService.getList(pageRequest);

        //then
        assertEquals(5L, findPosts.size());
        assertEquals("제목 30", findPosts.get(0).getTitle());
        assertEquals("제목 26", findPosts.get(4).getTitle());
    }
}