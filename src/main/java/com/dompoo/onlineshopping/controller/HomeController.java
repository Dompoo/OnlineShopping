package com.dompoo.onlineshopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String main() {
        return "메인페이지입니다.";
    }
}
