package com.dompoo.onlineshopping.repository.productRepository;

import com.dompoo.onlineshopping.domain.Product;
import com.dompoo.onlineshopping.request.product.ProductSearch;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getList(ProductSearch productSearch);
}
