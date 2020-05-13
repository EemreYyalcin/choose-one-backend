package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.KeyList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserPairsRepository {

    private final String KEY = "user-pairs";

    private final ReactiveRedisTemplate<String, KeyList> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, KeyList> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void save(KeyList keyList, String username) {
        reactiveHashOperations().put(KEY, username, keyList).subscribe();
    }

    public void addPairs(String username, String pairKey) {
        reactiveHashOperations().get(KEY, username)
                .map(e -> {
                    e.getKeys().add(pairKey);
                    return e;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    this.save(new KeyList().addKey(pairKey), username);
                    return Mono.empty();
                })).subscribe();
    }

    public Mono<KeyList> getPairs(String username) {
        return reactiveHashOperations().get(KEY, username);
    }


}
