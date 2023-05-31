package com.redisdemo02.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class redisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> temp = new RedisTemplate<>();

        temp.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);

        temp.setKeySerializer(new StringRedisSerializer());
        temp.setValueSerializer(jackson2JsonRedisSerializer);

        temp.setHashKeySerializer(new StringRedisSerializer());
        temp.setHashValueSerializer(jackson2JsonRedisSerializer);

        return temp;
    }

}
