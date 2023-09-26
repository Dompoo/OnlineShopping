package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.ProductEditor;
import com.dompoo.onlineshopping.repository.ProductRepository;
import com.dompoo.onlineshopping.request.ProductCreateRequest;
import com.dompoo.onlineshopping.request.ProductEditRequest;
import com.dompoo.onlineshopping.request.ProductSearch;
import com.dompoo.onlineshopping.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ProductResponse> getList(ProductSearch productSearch) {
        return productRepository.getList(productSearch).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long productId, ProductEditRequest productEdit) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        ProductEditor.ProductEditorBuilder editorBuilder = findProduct.toEditor();

        if (productEdit.getProductName() != null) {
            editorBuilder.productName(productEdit.getProductName());
        }

        if (productEdit.getPrice() > 0) {
            editorBuilder.price(productEdit.getPrice());
        }

        findProduct.edit(editorBuilder.build());
    }

    public void delete(Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        productRepository.delete(findProduct);
    }
}
