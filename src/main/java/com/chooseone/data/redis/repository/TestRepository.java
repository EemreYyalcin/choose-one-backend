package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.test.TestDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TestRepository {

    private String KEY = "TEST";

    private final ReactiveRedisTemplate<String, TestDocument> reactiveRedisTemplate;

    private final SortedSetRepository sortedSetRepository;

    private ReactiveHashOperations<String, String, TestDocument> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void save(TestDocument testDocument, int index) {
        reactiveHashOperations().put(KEY, testDocument.toString(), testDocument)
                .doOnNext(e -> sortedSetRepository.save(KEY, testDocument.toString(), index)).subscribe();
    }

    public Flux<TestDocument> getAll() {
        return sortedSetRepository.getAll(KEY).flatMap(e -> reactiveHashOperations().get(KEY, e.getId()));
    }

    public Mono<TestDocument> getOne(String key) {
        return reactiveHashOperations().get(KEY, key);
    }


}
