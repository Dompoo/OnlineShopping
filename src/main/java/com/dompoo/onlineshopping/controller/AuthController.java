package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.config.data.UserSession;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.dompoo.onlineshopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

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
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        String accessToken = authService.signin(request);

        ResponseCookie cookie = ResponseCookie
                .from("SESSION", accessToken)
                .domain("localhost") //TODO 서버 환경에 따른 분리 필요
                .path("/") //특정 서블릿에만 쿠키를 전달할 것인가?
                .httpOnly(true) //자바스크립트를 통한 쿠키탈취를 방어할 것인가?
                .secure(false) //https 요청에만 쿠키를 전달할 것인가?
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict") //서드파티요청에 쿠키를 전달할 것인가?
                .build();

        log.info(">> cookie = {}", cookie);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
