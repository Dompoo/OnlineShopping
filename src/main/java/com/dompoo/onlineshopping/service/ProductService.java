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

    /**
     * <pre>
     * 설명 : 새로운 상품을 등록합니다.
     *
     * 동작 : 상품을 등록하는 유저의 userId와
     * 상품 정보인 ProductCreateRequest가 주어지면,
     * 등록하는 유저를 찾고, request를 기반으로 상품을 생성합니다.
     * 그 후 레포지토리에 저장합니다.
     */
    public void add(ProductCreateRequest request, Long userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Product product = Product.builder()
                .productName(request.getProductName())
                .price(request.getPrice())
                .user(loginUser)
                .build();

        productRepository.save(product);
    }

    /**
     * <pre>
     * 설명 : 특정 productId의 상품을 검색합니다.
     *
     * 동작 : productId를 통해 상품을 검색하고,
     * ProductResponse객체로 변환하여 리턴합니다.
     */
    public ProductResponse get(Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);

        return ProductResponse.builder()
                .id(findProduct.getId())
                .productName(findProduct.getProductName())
                .price(findProduct.getPrice())
                .build();
    }

    /**
     * <pre>
     * 설명 : 특정 페이지의 모든 상품을 검색합니다.
     *
     * 동작 : ProductSearch를 통해 특정 페이지의 상품을 검색하고,
     * ProductResponse객체로 변환하여 리턴합니다.
     */
    public List<ProductResponse> getList(ProductSearch productSearch) {
        return productRepository.getList(productSearch).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * 설명 : 특정Id의 상품을 수정합니다.
     *
     * 동작 : 수정할 상품의 productId와 수정할 내용인 ProductEditRequest가 주어지면,
     * productId의 상품을 찾고, 그 상품의 Editor를 찾습니다.
     * request에 담긴 수정사항을 Editor에 적용하고,
     * 수정할 상품에 Editor를 적용합니다.
     */
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

    /**
     * <pre>
     * 설명 : 특정Id의 상품을 삭제합니다.
     *
     * 동작 : productId를 통해 상품을 검색하고,
     * 만약 존재한다면 삭제합니다.
     */
    public void delete(Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);
        productRepository.delete(findProduct);
    }
}
