package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreateRequest request) {

        postService.write(request);
        return Map.of();
    }
}
