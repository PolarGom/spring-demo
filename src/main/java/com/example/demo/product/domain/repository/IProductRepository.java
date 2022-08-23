package com.example.demo.product.domain.repository;

import com.example.demo.product.domain.Product;

import java.util.Optional;

public interface IProductRepository {

    Optional<Product> findById(int key);

    void save(Product product);

    void buyForRedisLock(int key, int buyCount);

    void buyForRedisNoLock(int key, int buyCount);
}
