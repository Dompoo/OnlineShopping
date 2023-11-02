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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.onlineshopping.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class ProductControllerDocTest {

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
    @DisplayName("상품 등록")
    void test1() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        this.mockMvc.perform(post("/products")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-craete",
                        requestFields(
                                fieldWithPath("productName").description("상품 이름").optional(),
                                fieldWithPath("price").description("상품 가격").optional()
                        )
                ));
    }

    @Test
    @DisplayName("상품 단건 조회")
    void test2() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        //expected
        this.mockMvc.perform(get("/products/{productId}", savedProduct.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-getOne",
                        pathParameters(
                                parameterWithName("productId").description("상품 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 ID"),
                                fieldWithPath("productName").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격")
                        )
                ));
    }

    @Test
    @DisplayName("상품 다건 조회")
    void test3() throws Exception {
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        List<Product> reqeustProducts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품이름 " + i)
                        .price(10000 * i)
                        .user(savedUser)
                        .build()
                )
                .toList();
        productRepository.saveAll(reqeustProducts);

        //expected
        mockMvc.perform(get("/products?page=1&size=5")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-getList",
                        responseFields(
                                fieldWithPath("[].id").description("상품 ID"),
                                fieldWithPath("[].productName").description("상품 제목"),
                                fieldWithPath("[].price").description("상품 가격")
                        )
                ));
    }

    @Test
    @MyMockUser
    @Transactional
    @DisplayName("상품 수정")
    void tets4() throws Exception{
        //given
        User findUser = userRepository.findAll().get(0);

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(findUser)
                .build());

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .build();
        String json = objectMapper.writeValueAsString(productEditRequest);

        //expected
        mockMvc.perform(patch("/products/{productId}", savedProduct.getId())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("product-update",
                        pathParameters(
                                parameterWithName("productId").description("업데이트할 상품 ID")
                        ),
                        requestFields(
                                fieldWithPath("productName").description("업데이트할 상품 이름"),
                                fieldWithPath("price").description("업데이트할 상품 가격")
                        )
                ));
    }

    @Test
    @MyMockUser
    @DisplayName("상품 삭제")
    void test5() throws Exception{
        //given
        User savedUser = userRepository.save(testUtil.newUserBuilder()
                .build());

        Product savedProduct = productRepository.save(testUtil.newProductBuilder()
                .user(savedUser)
                .build());

        //expected
        mockMvc.perform(delete("/products/{productId}", savedProduct.getId())
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("product-delete",
                        pathParameters(
                                parameterWithName("productId").description("삭제할 상품 ID")
                        )
                ));

    }
}
