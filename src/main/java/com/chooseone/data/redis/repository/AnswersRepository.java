package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.test.AnswersDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AnswersRepository {

    private final ReactiveRedisTemplate<String, AnswersDocument> reactiveRedisTemplate;

    private final SortedSetRepository sortedSetRepository;

    private ReactiveHashOperations<String, String, AnswersDocument> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void findAndUpdate(String KEY, String username, Integer response) {
        reactiveHashOperations().get(KEY, username)
                .doOnNext(e -> reactiveHashOperations().put(KEY, username, e.setAnswers(e.getAnswers() + response)))
                .thenReturn(Boolean.TRUE)
                .switchIfEmpty(Mono.defer(() -> reactiveHashOperations().put(KEY, username, new AnswersDocument().setAnswers("" + response)))).subscribe();

    }

}
