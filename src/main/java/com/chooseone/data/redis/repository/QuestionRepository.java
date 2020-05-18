package com.chooseone.data.redis.repository;

import com.chooseone.data.redis.model.test.QuestionDocument;
import com.chooseone.data.redis.model.test.TestDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QuestionRepository {

    private final ReactiveRedisTemplate<String, QuestionDocument> reactiveRedisTemplate;

    private final SortedSetRepository sortedSetRepository;

    private ReactiveHashOperations<String, String, QuestionDocument> reactiveHashOperations() {
        return reactiveRedisTemplate.opsForHash();
    }

    public void save(String KEY, String key, QuestionDocument questionDocument, int index) {
        reactiveHashOperations().put(KEY, key, questionDocument)
                .doOnNext(e -> sortedSetRepository.save(KEY, key, index)).subscribe();
    }

    public Mono<QuestionDocument> findNextQuestion(String KEY, int index){
        return sortedSetRepository.getNext(KEY, Long.valueOf(index))
                .flatMap(e -> reactiveHashOperations().get(KEY, e.getId()));
    }


}
