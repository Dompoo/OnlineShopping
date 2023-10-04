package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.request.PostEditRequest;
import com.dompoo.onlineshopping.request.PostSearch;
import com.dompoo.onlineshopping.response.PostResponse;
import com.dompoo.onlineshopping.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreateRequest request) {
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void patch(@PathVariable Long postId, @RequestBody @Valid PostEditRequest postEditRequest) {
        postService.edit(postId, postEditRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
