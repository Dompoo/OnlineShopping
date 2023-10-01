package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.exception.userException.AlreadyExistsEmailException;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

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
        assertEquals("1234", findUser.getPassword());
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

}