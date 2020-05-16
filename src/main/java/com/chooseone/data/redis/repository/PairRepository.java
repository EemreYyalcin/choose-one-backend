package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    public Mono<Pair> getPairFromPairs(String parentPair){
        return reactiveHashOperations().entries(KEY)
                .map(e -> e.getValue().setId(e.getKey()))
                .filter(e -> e.getParentPairs().equals(parentPair))
                .filter(e -> !e.isResolved()).next().switchIfEmpty(Mono.empty());
    }

    public Mono<Pair> updateAndGetNextPair(String pairId, String username, Integer click){
        return reactiveHashOperations().get(KEY, pairId)
                .map(e -> e.setClient1(username).setResolved(true).setClient1Click(click).setId(pairId))
                .doOnNext(e -> save(e, pairId))
                .flatMap(e -> getPairFromPairs(e.getParentPairs()));
    }

    public Integer getRate(String username, Integer click1){
        //TODO: calculate Rate
        return 45;
    }


}
