package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.exception.productException.ProductNotFound;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.product.ProductCreateRequest;
import com.dompoo.onlineshopping.request.product.ProductEditRequest;
import com.dompoo.onlineshopping.request.product.ProductSearch;
import com.dompoo.onlineshopping.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtil testUtil;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 추가")
    @Transactional
    void add() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder().build());

        ProductCreateRequest request = ProductCreateRequest.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();

        //when
        productService.add(request, addUser.getId());

        //then
        assertEquals(1L, productRepository.count());
        Product findProduct = productRepository.findAll().get(0);
        assertEquals("상품이름입니다.", findProduct.getProductName());
        assertEquals(10000, findProduct.getPrice());
        assertEquals(addUser.getName(), findProduct.getUser().getName());
    }

    @Test
    @DisplayName("상품 1개 조회")
    void get() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder().build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        //when
        ProductResponse findProduct = productService.get(savedProduct.getId());

        //then
        assertNotNull(findProduct);
        assertEquals(savedProduct.getProductName(), findProduct.getProductName());
        assertEquals(savedProduct.getPrice(), findProduct.getPrice());
    }

    @Test
    @DisplayName("상품 1개 조회 실패")
    void get2() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        //expected
        ProductNotFound e = assertThrows(ProductNotFound.class, () ->
                productService.get(savedProduct.getId() + 1));
        assertEquals("존재하지 않는 상품입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    @Test
    @DisplayName("상품 여러개 조회")
    void getList() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        List<Product> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품 " + i)
                        .price(i * 1000)
                        .user(addUser)
                        .build()
                )
                .toList();
        productRepository.saveAll(requestPosts);

        ProductSearch productSearch = ProductSearch.builder()
                .size(10)
                .page(1)
                .build();

        //when
        List<ProductResponse> findPosts = productService.getList(productSearch);

        //then
        assertEquals(10L, findPosts.size());
        assertEquals("상품 30", findPosts.get(0).getProductName());
        assertEquals("상품 21", findPosts.get(9).getProductName());
    }

    @Test
    @DisplayName("상품 전체 수정")
    void edit1() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .price(20000)
                        .build();

        //when
        productService.edit(savedProduct.getId(), productEditRequest);

        //then
        Product findProduct = productRepository.findById(savedProduct.getId()).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
        assertEquals("새상품이름입니다.", findProduct.getProductName());
        assertEquals(20000, findProduct.getPrice());
    }

    @Test
    @DisplayName("상품 제목 수정")
    void edit2() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .build();

        //when
        productService.edit(savedProduct.getId(), productEditRequest);

        //then
        Product findProduct = productRepository.findById(savedProduct.getId()).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
        assertEquals("새상품이름입니다.", findProduct.getProductName());
        assertEquals(savedProduct.getPrice(), findProduct.getPrice());
    }

    @Test
    @DisplayName("상품 가격 수정")
    void edit3() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .price(20000)
                        .build();

        //when
        productService.edit(savedProduct.getId(), productEditRequest);

        //then
        Product findProduct = productRepository.findById(savedProduct.getId()).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
        assertEquals(savedProduct.getProductName(), findProduct.getProductName());
        assertEquals(20000, findProduct.getPrice());
    }

    @Test
    @DisplayName("상품 수정 실패")
    void edit4() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .price(20000)
                        .build();

        //expected
        ProductNotFound e = assertThrows(ProductNotFound.class, () ->
                productService.edit(savedProduct.getId() + 1, productEditRequest));
        assertEquals("존재하지 않는 상품입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    @Test
    @DisplayName("상품 삭제")
    void delete() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        //when
        productService.delete(savedProduct.getId());

        //then
        assertEquals(0, productRepository.count());
    }

    @Test
    @DisplayName("상품 삭제 실패")
    void delete2() {
        //given
        User addUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(addUser)
                .build());

        //expected
        ProductNotFound e = assertThrows(ProductNotFound.class, () ->
                productService.delete(savedProduct.getId() + 1));
        assertEquals("존재하지 않는 상품입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }
}