package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.ProductEditor;
import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.exception.productException.ProductNotFound;
import com.dompoo.onlineshopping.exception.userException.UserNotFound;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.repository.productRepository.ProductRepository;
import com.dompoo.onlineshopping.request.product.ProductCreateRequest;
import com.dompoo.onlineshopping.request.product.ProductEditRequest;
import com.dompoo.onlineshopping.request.product.ProductSearch;
import com.dompoo.onlineshopping.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public void add(ProductCreateRequest request, Long userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Product product = Product.builder()
                .productName(request.getProductName())
                .price(request.getPrice())
                .user(loginUser)
                .build();

        productRepository.save(product);
    }


    public ProductResponse get(Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);

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

    public void edit(Long productId, ProductEditRequest productEdit) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);

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
                .orElseThrow(ProductNotFound::new);
        productRepository.delete(findProduct);
    }
}
