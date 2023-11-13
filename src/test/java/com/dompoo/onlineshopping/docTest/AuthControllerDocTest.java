package com.dompoo.onlineshopping.docTest;

import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.auth.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.onlineshopping.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class AuthControllerDocTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-create",
                        requestFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        )
                ));
    }
}
