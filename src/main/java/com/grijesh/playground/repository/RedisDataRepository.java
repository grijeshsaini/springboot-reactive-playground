package com.grijesh.playground.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RedisDataRepository {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Autowired
    public RedisDataRepository(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    }

    public Mono<Boolean> save(String key, String value) {
        return reactiveStringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public Mono<String> find(String key) {
        return reactiveStringRedisTemplate.opsForValue().get(key);
    }
}
