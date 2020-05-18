package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.SortedSetId;
import com.chooseone.data.redis.model.test.TestDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SortedSetRepository {

    private final ReactiveRedisOperations<String, SortedSetId> reactiveRedisOperations;

    public void save(String KEY, String key, long score) {
        this.reactiveRedisOperations.opsForZSet().add("order_" + KEY, new SortedSetId(key), score * 10).subscribe();
    }

    public Mono<SortedSetId> getNext(String KEY, Long score){
        return this.reactiveRedisOperations.opsForZSet().range("order_" + KEY, Range.of(Range.Bound.inclusive(score + 1), Range.Bound.inclusive(score*10 ))).next();
    }

    public Mono<SortedSetId> getNewestNext(String KEY){
        return this.reactiveRedisOperations.opsForZSet().reverseRange("order_" + KEY, Range.of(Range.Bound.inclusive(Instant.now().toEpochMilli()), Range.Bound.inclusive(0L))).next();
    }

    public Flux<SortedSetId> getAll(String KEY){
        return this.reactiveRedisOperations.opsForZSet().range("order_" + KEY, Range.of(Range.Bound.inclusive(0L), Range.Bound.inclusive(10000L)));
    }


}
