package com.example.demo.product.infra.db;

import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.repository.IProductRepository;
import com.example.demo.product.infra.db.jpa.ProductJpaRepository;
import com.example.demo.product.infra.db.redis.ProductRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {

    private final ProductJpaRepository productJpaRepository;

    private final ProductRedisRepository productRedisRepository;

    @Override
    public Optional<Product> findById(int key) {

        return productJpaRepository.findById(key);
    }

    @Override
    public void save(Product product) {

        productJpaRepository.save(product);
    }

    @Override
    public void buyForRedisLock(int key, int buyCount) {

        productRedisRepository.buy(key, buyCount);
    }

    @Override
    public void buyForRedisNoLock(int key, int buyCount) {

        productRedisRepository.buyForNoLock(key, buyCount);
    }
}
