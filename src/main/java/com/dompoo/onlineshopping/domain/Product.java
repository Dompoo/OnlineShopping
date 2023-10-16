package com.dompoo.onlineshopping.domain;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Product(String productName, int price, User user) {
        this.productName = productName;
        this.price = price;
        setUser(user);
    }

    //연관관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.getProducts().add(this);
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
