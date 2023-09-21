package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreateRequest request) {
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public Post get(@PathVariable Long postId) {
        return postService.get(postId);
    }
}
