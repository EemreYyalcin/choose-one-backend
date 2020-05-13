package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.Pairs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PairsRepository {

    private final String KEY = "pairs";

    private final ReactiveRedisTemplate<String, Pairs> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, Pairs> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void save(Pairs pairs, String username) {
        reactiveHashOperations().put(KEY, username, pairs).subscribe();
    }

    public Mono<Pairs> findPairs(String username){
        return reactiveHashOperations().get(KEY, username);
    }


}
