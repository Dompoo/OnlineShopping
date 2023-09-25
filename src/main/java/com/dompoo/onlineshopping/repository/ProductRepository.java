package com.dompoo.onlineshopping.repository;

import com.dompoo.onlineshopping.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
