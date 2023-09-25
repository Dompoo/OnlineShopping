package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.repository.ProductRepository;
import com.dompoo.onlineshopping.request.ProductCreateRequest;
import com.dompoo.onlineshopping.request.ProductSearch;
import com.dompoo.onlineshopping.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 추가")
    void add() {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();

        //when
        productService.add(request);

        //then
        assertEquals(1L, productRepository.count());
        Product findProduct = productRepository.findAll().get(0);
        assertEquals("상품이름입니다.", findProduct.getProductName());
        assertEquals(10000, findProduct.getPrice());
    }

    @Test
    @DisplayName("상품 1개 조회")
    void get() {
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        Product savedProduct = productRepository.save(product);

        //when
        ProductResponse findProduct = productService.get(savedProduct.getId());

        //then
        assertNotNull(findProduct);
        assertEquals("상품이름입니다.", findProduct.getProductName());
        assertEquals(10000, findProduct.getPrice());
    }

    @Test
    @DisplayName("상품 여러개 조회")
    void getList() {
        //given
        List<Product> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품 " + i)
                        .price(i * 1000)
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

}