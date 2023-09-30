package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.domain.Session;
import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.repository.SessionRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void login1() throws Exception {
        //given
        userRepository.save(Users.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        LoginRequest request = LoginRequest.builder()
                .email("dompoo@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void login2() throws Exception {
        //given
        Users user = userRepository.save(Users.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        LoginRequest request = LoginRequest.builder()
                .email("dompoo@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Users findUser = userRepository.findById(user.getId())
                .orElseThrow(RuntimeException::new);

        //then
        Assertions.assertEquals(1L, findUser.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void login3() throws Exception {
        //given
        Users user = userRepository.save(Users.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        LoginRequest request = LoginRequest.builder()
                .email("dompoo@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다 /test")
    void login4() throws Exception {
        //given
        Users user = Users.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        //expected
        mockMvc.perform(get("/test")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void login5() throws Exception {
        //given
        Users user = Users.builder()
                .name("dompoo")
                .email("dompoo@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);


        //expected
        mockMvc.perform(get("/test")
                        .header("Authorization", UUID.randomUUID())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("인증이 필요합니다."))
                .andDo(print());
    }
}