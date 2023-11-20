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
import com.dompoo.onlineshopping.request.post.PostCreateRequest;
import com.dompoo.onlineshopping.request.post.PostEditRequest;
import com.dompoo.onlineshopping.request.post.PostSearch;
import com.dompoo.onlineshopping.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final ProductRepository productRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * <pre>
     * 설명 : 글을 작성합니다.
     *
     * 동작 : 유저와 상품을 레포지토리에서 찾고,
     * request의 정보를 통해 Post 객체를 생성한 후,
     * PostRepository에 저장합니다.
     */
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

    /**
     * <pre>
     * 설명 : 특정Id의 글을 검색합니다.
     *
     * 동작 : postId를 통해 글을 검색하고,
     * PostResponse객체로 변환하여 리턴합니다.
     */
    public PostResponse get(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(findPost.getId())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .build();
    }

    /**
     * <pre>
     * 설명 : 특정 페이지의 모든 글을 검색합니다.
     *
     * 동작 : PostSearch를 통해 특정 페이지의 글을 검색하고,
     * PostResponse객체로 변환하여 리턴합니다.
     */
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 설명 : 특정Id의 글을 수정합니다.
     *
     * 동작 : 수정할 글의 postId와 수정할 내용인 PostEditRequest가 주어지면,
     * postId의 글을 찾고, 그 글의 Editor를 만듭니다.
     * request에 담긴 수정사항을 Editor에 적용하고,
     * 수정할 글에 Editor를 적용합니다.
     */
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

    /**
     * <pre>
     * 설명 : 특정Id의 글을 삭제합니다.
     *
     * 동작 : postId를 통해 글을 찾고,
     * 존재한다면 삭제합니다.
     */
    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(findPost);
    }
}
