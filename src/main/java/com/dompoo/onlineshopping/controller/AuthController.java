package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.SignupRequest;
import com.dompoo.onlineshopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지 입니다.";
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupRequest signup) {
        authService.signup(signup);
    }
}
