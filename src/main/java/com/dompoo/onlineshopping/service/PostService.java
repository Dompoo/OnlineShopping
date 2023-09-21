package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.repository.PostRepository;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreateRequest postCreateRequest) {

        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .build();

        postRepository.save(post);
    }

    public Post get(Long postId) {

        return postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
    }
}
