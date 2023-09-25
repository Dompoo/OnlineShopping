package com.dompoo.onlineshopping.repository;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.request.ProductSearch;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getList(ProductSearch productSearch);
}
