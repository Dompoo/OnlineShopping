package com.dompoo.onlineshopping;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.PostCreateRequest;
import com.dompoo.onlineshopping.request.PostEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestUtil {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public User.UserBuilder newUserBuilder() {
        return User.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234");
    }

    public Post.PostBuilder newPostBuilder() {
        return Post.builder()
                .title("글제목입니다.")
                .content("글내용입니다.");
    }

    public Product.ProductBuilder newProductBuilder() {
        return Product.builder()
                .productName("상품이름")
                .price(10000);
    }

    public PostCreateRequest newPostCreateRequest() {
        return PostCreateRequest.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();
    }

    public PostEditRequest newPostEditRequest() {
        return PostEditRequest.builder()
                .title("새로운글제목입니다.")
                .content("새로운글내용입니다.")
                .build();
    }
}
