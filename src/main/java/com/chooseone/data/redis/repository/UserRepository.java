package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRepository {

    private final String KEY = "users";

    private final ReactiveRedisTemplate<String, UserInfo> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, UserInfo> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public Mono<UserInfo> findByUsername(String username) {
        return reactiveHashOperations().get(KEY, username);
    }

    public Mono<UserInfo> save(UserInfo user) {
        return reactiveHashOperations().put(KEY, user.getUsername(), user).thenReturn(user);
    }

}
