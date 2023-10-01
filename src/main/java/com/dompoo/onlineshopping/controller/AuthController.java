package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.config.data.UserSession;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.dompoo.onlineshopping.response.SessionResponse;
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

    @GetMapping("/test")
    public Long test(UserSession userSession) {
        log.info(">> userID : {}", userSession.id);
        return userSession.id;
    }

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody LoginRequest request) {
        String accessToken = authService.signin(request);
        return new SessionResponse(accessToken);
    }
}
