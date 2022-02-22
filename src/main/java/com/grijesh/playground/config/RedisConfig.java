package com.grijesh.playground.config;

import com.grijesh.playground.dto.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.host:localhost}")
    private String host;

    @Value("${redis.port:6379}")
    private Integer port;

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration(host, port);
    }

    @Bean
    public ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext.RedisSerializationContextBuilder<String, UserDetails> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());
        var serializationContext = builder
                .value(new Jackson2JsonRedisSerializer<>(UserDetails.class)).build();
        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }

}
