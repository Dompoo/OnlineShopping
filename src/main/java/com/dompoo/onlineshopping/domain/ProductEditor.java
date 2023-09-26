package com.dompoo.onlineshopping.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductEditor {

    private String productName;
    private int price;

    @Builder
    public ProductEditor(String productName, int price) {
        this.productName = productName;
        this.price = price;
    }
}
