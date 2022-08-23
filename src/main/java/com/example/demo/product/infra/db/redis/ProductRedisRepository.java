package com.example.demo.product.infra.db.redis;

import com.example.demo.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProductRedisRepository {

    private final RedissonClient redissonClient;

    private int getRemainCount(int key) {

        return (int) redissonClient.getAtomicLong(String.format("product-%d", key)).get();
    }

    private void updateRemainCount(int key, int remainCount) {

        redissonClient.getAtomicLong(String.format("product-%d", key)).set(remainCount);
    }

    public void buy(int key, int buyCount) throws CommonException {

        final String lockName = String.format("%d:lock-product", key);
        final RLock lock = redissonClient.getLock(lockName);
        final String worker = Thread.currentThread().getName();

        try {

            if ( !lock.tryLock(3, 3, TimeUnit.SECONDS) ) {

                return;
            }
            
            int remainCount = getRemainCount(key);
            int buyAfterRemainCount = remainCount - buyCount;

            log.info("{}님 조회 당시 {}", worker, remainCount);

            if ( remainCount != 0 && buyAfterRemainCount >= 0 ) {

                updateRemainCount(key, buyAfterRemainCount);
                log.info("{}님이 {} 개를 샀습니다.", worker, buyCount);
            } else {

                log.info("{}님이 재고가 없어 구매하지 못했습니다.");
            }
        } catch ( InterruptedException e ) {

            throw new CommonException("구입 중 오류가 발생하였습니다.", e);
        } finally {

            if ( lock != null && lock.isLocked() ) {

                lock.unlock();
            }
        }
    }

    public void buyForNoLock(int key, int buyCount) throws CommonException {

        final String worker = Thread.currentThread().getName();

        int remainCount = getRemainCount(key);
        int buyAfterRemainCount = remainCount - buyCount;

        log.info("{}님 조회 당시 {}", worker, remainCount);

        if ( remainCount != 0 && buyAfterRemainCount >= 0 ) {

            updateRemainCount(key, buyAfterRemainCount);
            log.info("{}님이 {} 개를 샀습니다.", worker, buyCount);
        } else {

            log.info("{}님이 재고가 없어 구매하지 못했습니다.");
        }
    }
}
