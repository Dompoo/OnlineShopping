package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.PostEditor;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.exception.postException.PostNotFound;
import com.dompoo.onlineshopping.exception.productException.ProductNotFound;
import com.dompoo.onlineshopping.exception.userException.UserNotFound;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.request.PostEditRequest;
import com.dompoo.onlineshopping.request.PostSearch;
import com.dompoo.onlineshopping.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final ProductRepository productRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void write(PostCreateRequest postCreateRequest, Long userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Product findProduct = productRepository.findById(postCreateRequest.getProductId()).orElseThrow(ProductNotFound::new);

        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .user(loginUser)
                .product(findProduct)
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(findPost.getId())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long postId, PostEditRequest postEdit) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = findPost.toEditor();

        if (postEdit.getTitle() != null) {
            editorBuilder.title(postEdit.getTitle());
        }

        if (postEdit.getContent() != null) {
            editorBuilder.content(postEdit.getContent());
        }

        findPost.edit(editorBuilder.build());
    }

    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(findPost);
    }
}
