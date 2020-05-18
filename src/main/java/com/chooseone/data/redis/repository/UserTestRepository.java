package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.test.UserTestDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserTestRepository {

    private final String KEY = "user_tests";

    private final ReactiveRedisTemplate<String, UserTestDocument> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, UserTestDocument> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void save(String key, UserTestDocument userTestDocument) {
        reactiveHashOperations().put(KEY, key, userTestDocument).subscribe();
    }

    public Mono<UserTestDocument> findAllNewestTests(String username) {
        return reactiveHashOperations().get(KEY, username);
    }
}
