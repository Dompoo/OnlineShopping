package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.auth.SignupRequest;
import com.dompoo.onlineshopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupRequest signup) {
        authService.signup(signup);
    }
}
