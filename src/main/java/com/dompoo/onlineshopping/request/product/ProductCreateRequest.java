package com.dompoo.onlineshopping.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductCreateRequest {

    @NotBlank(message = "상품이름을 입력해주세요.")
    private String productName;

    @PositiveOrZero(message = "상품가격을 입력해주세요.")
    private int price;

    @Builder
    public ProductCreateRequest(String productName, int price) {
        this.productName = productName;
        this.price = price;
    }
}
