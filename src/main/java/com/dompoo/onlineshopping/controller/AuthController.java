package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.exception.InvalidSigninInfo;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public Users login(@RequestBody LoginRequest request) {
        // json 아이디/비밀번호
        log.info(">> login : {}", request);

        // DB에서 조회
        Users findUser = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInfo::new);

        // 토큰을 응답
        return findUser;
    }
}
