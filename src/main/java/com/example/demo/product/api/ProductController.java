package com.example.demo.product.api;

import com.example.demo.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PatchMapping(value = "/buy/{key}/{buyCount}")
    public ResponseEntity buy(@PathVariable int key, @PathVariable int buyCount) {

        productService.buy(key, buyCount);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/buy/redis-lock/{key}/{buyCount}")
    public ResponseEntity buyForRedisLock(@PathVariable int key, @PathVariable int buyCount) {

        productService.buyForRedisLock(key, buyCount);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/buy/redis-no-lock/{key}/{buyCount}")
    public ResponseEntity buyForRedisNoLock(@PathVariable int key, @PathVariable int buyCount) {

        productService.buyForRedisNoLock(key, buyCount);

        return ResponseEntity.ok().build();
    }
}
