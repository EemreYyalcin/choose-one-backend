package com.chooseone.data.redis.config;


import com.chooseone.data.redis.model.*;
import com.chooseone.data.redis.model.test.AnswersDocument;
import com.chooseone.data.redis.model.test.QuestionDocument;
import com.chooseone.data.redis.model.test.TestDocument;
import com.chooseone.data.redis.model.test.UserTestDocument;
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


    @Bean
    public ReactiveRedisTemplate<String, Pairs> redisPairsOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Pairs> serializationContext
                = RedisSerializationContext
                .<String, Pairs>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(Pairs.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }


    @Bean
    public ReactiveRedisTemplate<String, Pair> redisPairOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Pair> serializationContext
                = RedisSerializationContext
                .<String, Pair>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(Pair.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, KeyList> redisKeyListOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, KeyList> serializationContext
                = RedisSerializationContext
                .<String, KeyList>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(KeyList.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, TestDocument> redisTestDocumentOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, TestDocument> serializationContext
                = RedisSerializationContext
                .<String, TestDocument>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(TestDocument.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }


    @Bean
    public ReactiveRedisTemplate<String, QuestionDocument> redisQuestionDocumentOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, QuestionDocument> serializationContext
                = RedisSerializationContext
                .<String, QuestionDocument>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(QuestionDocument.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }


    @Bean
    public ReactiveRedisTemplate<String, UserTestDocument> redisUserTestsDocumentOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, UserTestDocument> serializationContext
                = RedisSerializationContext
                .<String, UserTestDocument>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(UserTestDocument.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, AnswersDocument> redisAnswersDocumentOperations(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, AnswersDocument> serializationContext
                = RedisSerializationContext
                .<String, AnswersDocument>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(configureJackson2JsonRedisSerializer(AnswersDocument.class))
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }



}
