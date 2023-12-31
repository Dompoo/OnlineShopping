package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.TestUtil;
import com.dompoo.onlineshopping.config.MyMockUser;
import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.product.ProductCreateRequest;
import com.dompoo.onlineshopping.request.product.ProductEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
@Slf4j
class ProductControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtil testUtil;
    @Autowired private EntityManager em;

    @AfterEach
    void clean() {
        em.clear();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("글 작성")
    void addProduct1() throws Exception {
        //given
        ProductCreateRequest product = ProductCreateRequest.builder()
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
    @DisplayName("상품 1개 조회")
    void get1() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        //expected
        mockMvc.perform(get("/products/{productId}", product.getId()))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 상품 1개 조회")
    void get2() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        //expected
        mockMvc.perform(get("/products/{productId}", product.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 여러개 조회")
    void getList1() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        List<Product> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품 " + i)
                        .price(i * 1000)
                        .user(savedUser)
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
    void getList2() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        List<Product> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품 " + i)
                        .price(i * 1000)
                        .user(savedUser)
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
    @MyMockUser
    @Transactional
    @DisplayName("상품 이름 수정, DB값 변경")
    void patch1() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .build();
        String json = objectMapper.writeValueAsString(productEditRequest);

        //when
        mockMvc.perform(patch("/products/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Product findProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 상품입니다.")
        );
        assertEquals("새상품이름입니다.", findProduct.getProductName());
        assertEquals(product.getPrice(), findProduct.getPrice());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("상품 가격 수정, DB값 변경")
    void patch2() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .price(20000)
                        .build();
        String json = objectMapper.writeValueAsString(productEditRequest);

        //when
        mockMvc.perform(patch("/products/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Product findProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 상품입니다.")
        );
        assertEquals(product.getProductName(), findProduct.getProductName());
        assertEquals(20000, findProduct.getPrice());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("존재하지 않는 상품 수정")
    void patch3() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .price(20000)
                        .build();
        String json = objectMapper.writeValueAsString(productEditRequest);

        //expected
        mockMvc.perform(patch("/products/{productId}", product.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."))
                .andDo(print());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("상품 삭제, DB값 변경")
    void delete1() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        //when
        mockMvc.perform(delete("/products/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(0L, productRepository.count());
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("존재하지 않는 상품 삭제")
    void delete2() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product product = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        //expected
        mockMvc.perform(delete("/products/{productId}", product.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."))
                .andDo(print());
    }

}