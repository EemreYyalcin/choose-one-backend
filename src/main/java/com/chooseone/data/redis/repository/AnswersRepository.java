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

    private ReactiveHashOperations<String, String, AnswersDocument> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }


    public void saveAnswer(String KEY, String questionId, Integer response, String username) {
        reactiveHashOperations().get(KEY, questionId + "_" + response)
                .doOnNext(e -> reactiveHashOperations().put(KEY, questionId + "_" + response, e.addUser(username)).subscribe())
                .thenReturn(Boolean.TRUE)
                .switchIfEmpty(Mono.defer(() -> {
                    reactiveHashOperations().put(KEY, questionId + "_" + response, new AnswersDocument().addUser(username)).subscribe();
                    return Mono.just(Boolean.TRUE);
                }));

    }

}
