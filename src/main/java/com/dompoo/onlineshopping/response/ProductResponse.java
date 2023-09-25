package com.dompoo.onlineshopping.response;

import com.dompoo.onlineshopping.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long id;
    private final String productName;
    private final int price;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
    }

    @Builder
    public ProductResponse(Long id, String productName, int price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
    }
}
