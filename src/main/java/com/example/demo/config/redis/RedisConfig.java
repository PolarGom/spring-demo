package com.example.demo.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public String port;

    @Value("${spring.redis.password}")
    public String password;

    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", host, port))
                .setPassword(password)
                .setConnectionMinimumIdleSize(5)
                .setConnectionPoolSize(50);

        return Redisson.create(config);
    }
}
