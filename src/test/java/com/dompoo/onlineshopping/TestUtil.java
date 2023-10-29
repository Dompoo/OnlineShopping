package com.dompoo.onlineshopping;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestUtil {

    private static Long count = 0L;

    public User.UserBuilder newUserBuilder() {
        return User.builder()
                .name("dompoo" + count++)
                .email("dompoo@gmail.com" + count++)
                .password("1234" + count++);
    }

    public User.UserBuilder newUserBuilderPlain() {
        return User.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234");
    }

    public Post.PostBuilder newPostBuilder() {
        return Post.builder()
                .title("글제목입니다." + count++)
                .content("글내용입니다." + count++);
    }

    public Product.ProductBuilder newProductBuilder() {
        return Product.builder()
                .productName("상품이름입니다." + count++)
                .price((int) (10000 + count++));
    }
}
