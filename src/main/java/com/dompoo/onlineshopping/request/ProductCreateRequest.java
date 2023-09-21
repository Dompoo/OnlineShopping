package com.dompoo.onlineshopping.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

}
