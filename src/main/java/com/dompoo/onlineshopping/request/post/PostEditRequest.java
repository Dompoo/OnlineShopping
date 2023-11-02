package com.dompoo.onlineshopping.request.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostEditRequest {

    private String title;
    private String content;

    @Builder
    public PostEditRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
