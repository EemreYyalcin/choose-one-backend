package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PairRepository {

    private final String KEY = "pair";

    private final ReactiveRedisTemplate<String, Pair> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, Pair> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void save(Pair pair, String key) {
        reactiveHashOperations().put(KEY, key, pair).subscribe();
    }

}
