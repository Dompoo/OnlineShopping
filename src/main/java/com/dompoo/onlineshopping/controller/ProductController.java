package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.request.ProductCreateRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class ProductController {

    @PostMapping("/addProduct")
    public Map<String, String> addProduct(@RequestBody @Valid ProductCreateRequest params) {

        log.info("params={}", params.toString());

        return Map.of();
    }

}
