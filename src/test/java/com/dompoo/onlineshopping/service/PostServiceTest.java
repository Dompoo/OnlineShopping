package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.repository.PostRepository;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.request.PostEditRequest;
import com.dompoo.onlineshopping.request.PostSearch;
import com.dompoo.onlineshopping.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        //when
        List<PostResponse> findPosts = postService.getList(postSearch);

        //then
        assertEquals(10L, findPosts.size());
        assertEquals("제목 30", findPosts.get(0).getTitle());
        assertEquals("제목 26", findPosts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void edit() {
        //given
        Post post = Post.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();
        postRepository.save(post);

        PostEditRequest postEditRequest = PostEditRequest.builder()
                .title("새로운글제목입니다.")
                .build();

        //when
        postService.edit(post.getId(), postEditRequest);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("새로운글제목입니다.", changedPost.getTitle());
        assertEquals("글내용입니다.", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void edit2() {
        //given
        Post post = Post.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();
        postRepository.save(post);

        PostEditRequest postEditRequest = PostEditRequest.builder()
                .content("새로운글내용입니다.")
                .build();

        //when
        postService.edit(post.getId(), postEditRequest);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("글제목입니다.", changedPost.getTitle());
        assertEquals("새로운글내용입니다.", changedPost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void delete() {
        //given
        Post post = Post.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();
        postRepository.save(post);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        //when
        postService.delete(post.getId());

        //then
        assertThrows(IllegalArgumentException.class, () -> postService.get((post.getId())));
        assertEquals(0, postService.getList(postSearch).size());
    }
}