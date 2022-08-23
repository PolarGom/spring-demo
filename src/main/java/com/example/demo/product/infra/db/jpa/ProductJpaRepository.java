package com.example.demo.product.infra.db.jpa;

import com.example.demo.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {
}
