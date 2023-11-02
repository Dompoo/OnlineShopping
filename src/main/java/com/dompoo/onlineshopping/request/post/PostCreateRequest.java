package com.dompoo.onlineshopping.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreateRequest {

    @NotBlank(message = "글제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "글내용를 입력해주세요.")
    private String content;

    @NotNull(message = "상품이 선택되지 않았습니다.")
    private Long productId;

    @Builder
    public PostCreateRequest(String title, String content, Long productId) {
        this.title = title;
        this.content = content;
        this.productId = productId;
    }
}
