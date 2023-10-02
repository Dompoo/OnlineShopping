package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.crypto.PasswordEncoder;
import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.exception.userException.AlreadyExistsEmailException;
import com.dompoo.onlineshopping.exception.userException.InvalidSigninInfo;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.dompoo.onlineshopping.request.SignupRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;


    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("회원가입 성공")
    void signup1() {
        //given
        SignupRequest request = SignupRequest.builder()
                .name("dompoo")
                .password("1234")
                .email("dompoo@gmail.com")
                .build();

        //when
        authService.signup(request);

        //then
        assertEquals(1L, userRepository.count());
        Users findUser = userRepository.findAll().iterator().next();
        assertEquals("dompoo", findUser.getName());
        assertTrue(encoder.matches("1234", findUser.getPassword()));
        assertNotNull(findUser.getPassword());
        assertEquals("dompoo@gmail.com", findUser.getEmail());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일은 실패한다.")
    void signup2() {
        //given
        userRepository.save(Users.builder()
                .name("oopmod")
                .password("4321")
                .email("dompoo@gmail.com")
                .build());

        SignupRequest request = SignupRequest.builder()
                .name("dompoo")
                .password("1234")
                .email("dompoo@gmail.com")
                .build();

        //expected
        assertThrows(AlreadyExistsEmailException.class, () ->
                authService.signup(request));
    }

    @Test
    @DisplayName("로그인 성공")
    void login1() {
        //given
        Users users = Users.builder()
                .name("dompoo")
                .password(encoder.encrypt("1234"))
                .email("dompoo@gmail.com")
                .build();
        userRepository.save(users);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("dompoo@gmail.com")
                .password("1234")
                .build();

        //when
        Long userId = authService.signin(loginRequest);

        //then
        Assertions.assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인 실패")
    void login2() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .name("dompoo")
                .password("1234")
                .email("dompoo@gmail.com")
                .build();
        authService.signup(signupRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("dompoo@gmail.com")
                .password("5678")
                .build();

        //expected
        Assertions.assertThrows(InvalidSigninInfo.class, () ->
                authService.signin(loginRequest));
    }

}