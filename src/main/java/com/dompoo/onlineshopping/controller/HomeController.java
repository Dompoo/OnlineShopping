package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.config.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {

    @GetMapping("/")
    public String main() {
        return "메인 페이지입니다.";
    }

    @PreAuthorize("hasRole('ROLE_USER')") // Spring EL
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("{}, {}", userPrincipal.getUsername(), userPrincipal.getPassword());

        return "사용자 페이지입니다.";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지입니다.";
    }
}
