package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.repository.ProductRepository;
import com.dompoo.onlineshopping.request.ProductEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("/products 요청시 {}를 출력한다.")
    void test() throws Exception {
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        String json = objectMapper.writeValueAsString(product);

        //expected
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/products 요청시 productName값은 필수다.")
    void test2() throws Exception {
        //given
        Product product = Product.builder()
                .price(10000)
                .build();
        String json = objectMapper.writeValueAsString(product);

        //expected
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.productName").value("상품이름을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/products 요청시 DB에 저장된다.")
    void test3() throws Exception {
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        String json = objectMapper.writeValueAsString(product);

        //when
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, productRepository.findAll().size());
        Product findProduct = productRepository.findAll().get(0);
        assertEquals("상품이름입니다.", findProduct.getProductName());
        assertEquals(10000, findProduct.getPrice());
    }
    
    @Test
    @DisplayName("상품 1개 조회")
    void test4() throws Exception {
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        productRepository.save(product);

        //expected
        mockMvc.perform(get("/products/{productId}", product.getId()))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productName").value("상품이름입니다."))
                .andExpect(jsonPath("$.price").value(10000))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 여러개 조회")
    void test5() throws Exception {
        //given
        List<Product> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품 " + i)
                        .price(i * 1000)
                        .build()
                )
                .toList();
        productRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/products?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].productName").value("상품 30"))
                .andExpect(jsonPath("$[0].price").value(30000))
                .andExpect(jsonPath("$[4].productName").value("상품 26"))
                .andExpect(jsonPath("$[4].price").value(26000))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 여러개 조회시 페이지를 0으로 조회해도 첫 페이지를 가져온다.")
    void test6() throws Exception {
        //given
        List<Product> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품 " + i)
                        .price(i * 1000)
                        .build()
                )
                .toList();
        productRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/products?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].productName").value("상품 30"))
                .andExpect(jsonPath("$[0].price").value(30000))
                .andExpect(jsonPath("$[4].productName").value("상품 26"))
                .andExpect(jsonPath("$[4].price").value(26000))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정")
    void teset7() throws Exception {
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        productRepository.save(product);

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .price(20000)
                        .build();
        String json = objectMapper.writeValueAsString(productEditRequest);


        //expected
        mockMvc.perform(patch("/products/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}