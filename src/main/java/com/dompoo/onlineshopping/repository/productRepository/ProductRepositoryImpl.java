package com.dompoo.onlineshopping.repository.productRepository;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.domain.QProduct;
import com.dompoo.onlineshopping.request.ProductSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Product> getList(ProductSearch productSearch) {
        return jpaQueryFactory
                .selectFrom(QProduct.product)
                .limit(productSearch.getSize())
                .offset(productSearch.getOffset())
                .orderBy(QProduct.product.id.desc())
                .fetch();
    }
}
