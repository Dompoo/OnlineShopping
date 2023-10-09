package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.ProductCreateRequest;
import com.dompoo.onlineshopping.request.ProductEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(username = "dompoo@gmail.com", password = "1234", roles = {"ADMIN"})
    @DisplayName("상품 등록")
    void test1() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();

        String json = objectMapper.writeValueAsString(request);
        //given

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
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        productRepository.save(product);

        //expected
        this.mockMvc.perform(get("/products/{productId}", product.getId())
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
        List<Product> reqeustProducts = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .productName("상품이름 " + i)
                        .price(10000 * i)
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
    @WithMockUser(username = "dompoo@gmail.com", password = "1234", roles = {"ADMIN"})
    @DisplayName("상품 수정")
    void tets4() throws Exception{
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        productRepository.save(product);

        ProductEditRequest productEditRequest =
                ProductEditRequest.builder()
                        .productName("새상품이름입니다.")
                        .build();
        String json = objectMapper.writeValueAsString(productEditRequest);

        //expected
        mockMvc.perform(patch("/products/{productId}", product.getId())
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
    @WithMockUser(username = "dompoo@gmail.com", password = "1234", roles = {"ADMIN"})
    @DisplayName("상품 삭제")
    void test5() throws Exception{
        //given
        Product product = Product.builder()
                .productName("상품이름입니다.")
                .price(10000)
                .build();
        productRepository.save(product);

        //expected
        mockMvc.perform(delete("/products/{productId}", product.getId())
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
