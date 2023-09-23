package com.dompoo.onlineshopping.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 100;
    private static final int MIN_PAGE = 1;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return ((long) (Math.max(MIN_PAGE, page) - 1) * Math.min(size, MAX_SIZE));
    }

//    @Builder
//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }
}
