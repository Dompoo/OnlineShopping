package com.dompoo.onlineshopping.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private int price;

    @Builder
    public Product(String productName, int price) {
        this.productName = productName;
        this.price = price;
    }

    public ProductEditor.ProductEditorBuilder toEditor() {
        return ProductEditor.builder()
                .productName(productName)
                .price(price);
    }

    public void edit(ProductEditor productEditor) {
        productName = productEditor.getProductName();
        price = productEditor.getPrice();
    }
}
