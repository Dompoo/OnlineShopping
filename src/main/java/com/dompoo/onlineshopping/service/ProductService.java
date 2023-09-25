package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.repository.ProductRepository;
import com.dompoo.onlineshopping.request.ProductCreateRequest;
import com.dompoo.onlineshopping.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void add(ProductCreateRequest request) {
        Product product = Product.builder()
                .productName(request.getProductName())
                .price(request.getPrice())
                .build();

        productRepository.save(product);
    }


    public ProductResponse get(Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return ProductResponse.builder()
                .id(findProduct.getId())
                .productName(findProduct.getProductName())
                .price(findProduct.getPrice())
                .build();
    }
}
