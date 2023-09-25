package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}