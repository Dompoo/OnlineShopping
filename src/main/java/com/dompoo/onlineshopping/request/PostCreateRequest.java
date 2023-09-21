package com.dompoo.onlineshopping.request;

import jakarta.validation.constraints.NotBlank;
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

}
