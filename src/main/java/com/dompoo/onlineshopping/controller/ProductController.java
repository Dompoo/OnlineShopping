package com.dompoo.onlineshopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/posts")
    public String get() {
        return "Hello, world!";
    }

}
