package com.chooseone.data.redis.config;


import com.chooseone.data.redis.model.Imdb;
import com.chooseone.data.redis.model.SortedSetId;
import com.chooseone.data.redis.model.UserInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext;

@Configuration
public class RedisConfiguration {

    public <T> Jackson2JsonRedisSerializer<T> configureJackson2JsonRedisSerializer(Class<T> t) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(t);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    public ReactiveRedisOperations<String, SortedSetId> redisSortedSetOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<SortedSetId> serializer = configureJackson2JsonRedisSerializer(SortedSetId.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, SortedSetId> builder =
                newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, SortedSetId> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, UserInfo> redisUserInfoOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, UserInfo> serializationContext
                = RedisSerializationContext
                .<String, UserInfo>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(UserInfo.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, Imdb> redisImdbOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Imdb> serializationContext
                = RedisSerializationContext
                .<String, Imdb>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(Imdb.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }



}
