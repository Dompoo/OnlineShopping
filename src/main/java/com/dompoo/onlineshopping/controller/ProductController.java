package com.dompoo.onlineshopping.controller;

import com.dompoo.onlineshopping.config.UserPrincipal;
import com.dompoo.onlineshopping.request.ProductCreateRequest;
import com.dompoo.onlineshopping.request.ProductEditRequest;
import com.dompoo.onlineshopping.request.ProductSearch;
import com.dompoo.onlineshopping.response.ProductResponse;
import com.dompoo.onlineshopping.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products")
    public void addProduct(@RequestBody @Valid ProductCreateRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        productService.add(request, principal.getUserId());
    }

    @GetMapping("/products/{productId}")
    public ProductResponse get(@PathVariable Long productId) {
        return productService.get(productId);
    }

    @GetMapping("/products")
    public List<ProductResponse> getList(@ModelAttribute ProductSearch productSearch) {
        return productService.getList(productSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/products/{productId}")
    public void patch(@PathVariable Long productId, @RequestBody @Valid ProductEditRequest productEditRequest) {
        productService.edit(productId, productEditRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{productId}")
    public void delete(@PathVariable Long productId) {
        productService.delete(productId);
    }
}
