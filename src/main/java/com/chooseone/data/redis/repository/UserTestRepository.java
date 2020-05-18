package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.test.UserTestDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserTestRepository {

    private final String KEY = "user_tests";

    private final ReactiveRedisTemplate<String, UserTestDocument> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, UserTestDocument> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public Mono<UserTestDocument> updateAndGet(String key){
        return reactiveHashOperations().get(KEY, key)
                .map(e -> e.setIndex(e.getIndex() + 10).setLastUpdatedDate(LocalDateTime.now()))
                .doOnNext(e -> reactiveHashOperations().put(KEY, key, e))
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.defer(() -> {
                    UserTestDocument userTestDocument =  new UserTestDocument().setLastUpdatedDate(LocalDateTime.now());
                    reactiveHashOperations().put(KEY, key, userTestDocument).subscribe();
                    return Mono.just(userTestDocument);
                }));

    }

    public Mono<UserTestDocument> findAllNewestTests(String key) {
        return reactiveHashOperations().get(KEY, key);
    }
}
