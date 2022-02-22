package com.grijesh.playground.repository;

import com.grijesh.playground.dto.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RedisDataRepository {

    private final ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate;

    @Autowired
    public RedisDataRepository(ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Mono<Boolean> save(UserDetails userDetails) {
        return reactiveRedisTemplate.opsForValue().setIfAbsent(userDetails.shortName(), userDetails);
    }

    public Mono<UserDetails> find(String key) {
        return reactiveRedisTemplate.opsForValue().get(key);
    }
}
