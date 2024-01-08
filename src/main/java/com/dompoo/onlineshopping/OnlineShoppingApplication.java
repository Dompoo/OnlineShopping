package com.dompoo.onlineshopping;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.repository.ChatMessageRepository;
import com.dompoo.onlineshopping.repository.ChatRoomRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.postRepository.PostRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootApplication
public class OnlineShoppingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingApplication.class, args);
    }

    @Configuration
    @Profile("local")
    @RequiredArgsConstructor
    public class localDataInit {
        private final UserRepository userRepository;
        private final PostRepository postRepository;
        private final ProductRepository productRepository;
        private final ChatMessageRepository chatMessageRepository;
        private final ChatRoomRepository chatRoomRepository;
        private final PasswordEncoder encoder;

        @PostConstruct
        public void init() {
            log.info("테스트 데이터 init");

            User savedUser = userRepository.save(User.builder()
                    .email("dompoo@gmail.com")
                    .name("dompoo")
                    .password(encoder.encode("1234"))
                    .build());

            Product savedProduct = productRepository.save(Product.builder()
                    .productName("플스")
                    .price(120000)
                    .user(savedUser)
                    .build());

            postRepository.save(Post.builder()
                    .title("플스 팝니다")
                    .content("플스 팔아요 10만언")
                    .product(savedProduct)
                    .user(savedUser)
                    .build());
        }
    }
}
