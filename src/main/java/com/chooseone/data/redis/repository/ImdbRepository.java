package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.Imdb;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ImdbRepository {

    private final String KEY = "movies";

    private final ReactiveRedisTemplate<String, Imdb> reactiveRedisTemplate;

    private ReactiveHashOperations<String, String, Imdb> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public Mono<Imdb> findByImdb(String username) {
        return reactiveHashOperations().get(KEY, username);
    }

    public void save(Imdb imdb) {
         reactiveHashOperations().put(KEY, imdb.getImdbID(), imdb).subscribe();
    }

    public Flux<Imdb> getAllMovies(){
        return reactiveHashOperations().values(KEY);
    }


}
