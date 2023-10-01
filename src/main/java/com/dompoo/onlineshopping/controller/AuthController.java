package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.config.AppConfig;
import com.dompoo.onlineshopping.config.data.UserSession;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.dompoo.onlineshopping.response.SessionResponse;
import com.dompoo.onlineshopping.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @GetMapping("/test")
    public Long test(UserSession userSession) {
        log.info(">> userID : {}", userSession.id);
        return userSession.id;
    }

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody LoginRequest request) {
        Long userId = authService.signin(request);

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date())
                .compact();

        return SessionResponse.builder()
                .accessToken(jws)
                .build();
    }
}
