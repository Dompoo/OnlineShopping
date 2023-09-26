package com.dompoo.onlineshopping.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductEditRequest {

    private String productName;
    private int price;

    @Builder
    public ProductEditRequest(String productName, int price) {
        this.productName = productName;
        this.price = price;
    }
}
