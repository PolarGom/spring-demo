package com.example.demo.product.service;

public interface IProductService {

    void buy(int key, int buyCount);

    void buyForRedisLock(int key, int buyCount);

    void buyForRedisNoLock(int key, int buyCount);
}
