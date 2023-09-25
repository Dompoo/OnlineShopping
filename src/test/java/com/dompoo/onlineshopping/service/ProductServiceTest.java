package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.repository.ProductRepository;
import com.dompoo.onlineshopping.request.ProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .productName("제품1")
                .price(10000)
                .build();

        //when
        productService.add(request);

        //then
        assertEquals(1L, productRepository.count());
        Product findProduct = productRepository.findAll().get(0);
        assertEquals("제품1", findProduct.getProductName());
        assertEquals(10000, findProduct.getPrice());
    }

}