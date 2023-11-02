package com.dompoo.onlineshopping.request.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductSearch {

    private static final int MAX_SIZE = 100;
    private static final int MIN_PAGE = 1;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return ((long) (Math.max(MIN_PAGE, page) - 1) * Math.min(size, MAX_SIZE));
    }
}
