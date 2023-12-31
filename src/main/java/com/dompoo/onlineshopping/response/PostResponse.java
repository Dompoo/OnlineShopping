package com.dompoo.onlineshopping.response;

import com.dompoo.onlineshopping.domain.Post;
import lombok.Builder;
import lombok.Getter;

/**
 * Post응답은 10글자만 응답한다.
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle().substring(0, Math.min(post.getTitle().length(), 10));
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
