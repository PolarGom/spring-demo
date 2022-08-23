package com.example.demo.product.service;

import com.example.demo.product.domain.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;

    @Transactional
    @Override
    public void buy(int key, int buyCount) {

        String worker = Thread.currentThread().getName();

        productRepository.findById(key).ifPresent(product -> {

            log.info("{}님 조회 당시 {}", worker, product.remainStatus());

            if ( product.possibleBuy(buyCount) ) {

                product.buy(buyCount);
                log.info("{}님이 {} 개를 샀습니다.", worker, buyCount);
            } else {

                log.info("{}님이 재고가 없어 구매하지 못했습니다.");
            }
        });
    }

    @Override
    public void buyForRedisLock(int key, int buyCount) {

        productRepository.buyForRedisLock(key, buyCount);
    }

    @Override
    public void buyForRedisNoLock(int key, int buyCount) {

        productRepository.buyForRedisNoLock(key, buyCount);
    }
}
