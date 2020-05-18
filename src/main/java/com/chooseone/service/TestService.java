package com.chooseone.service;

import com.chooseone.data.redis.model.test.TestDocument;
import com.chooseone.data.redis.model.test.UserTestDocument;
import com.chooseone.data.redis.repository.AnswersRepository;
import com.chooseone.data.redis.repository.QuestionRepository;
import com.chooseone.data.redis.repository.TestRepository;
import com.chooseone.data.redis.repository.UserTestRepository;
import com.chooseone.model.request.TestsRequestModel;
import com.chooseone.model.response.QuestionResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final UserTestRepository userTestRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AnswersRepository answersRepository;

    private EmitterProcessor<QuestionResponseModel> emitterProcessor;
    private FluxSink<QuestionResponseModel> sink;

    @PostConstruct
    public void setup() {
        emitterProcessor = EmitterProcessor.create(10);
        sink = emitterProcessor.sink();
    }

    public Flux<UserTestDocument> getAllUserTests(String username) {
        return testRepository.getAll().flatMap(e -> userTestRepository.findAllNewestTests(username + "_" + e.toString()).doOnNext(q -> q.setTestDocument(e)));
    }

    public Flux<TestDocument> getAllTest() {
        return testRepository.getAll();
    }

    public Mono<Boolean> sentActiveTests(TestsRequestModel testsRequestModel, String username) {
        return testRepository.getOne(new TestDocument(testsRequestModel.getType(), testsRequestModel.getCategory()).toString())
                .doOnNext(t -> {
                    if (ObjectUtils.isEmpty(testsRequestModel.getQuestionId()) || testsRequestModel.getResponse() == 99) {
                        return;
                    }
                    log.info("Answer Saved:{}", testsRequestModel.getQuestionId());
                    answersRepository.saveAnswer("answer_" + t.toString(), testsRequestModel.getQuestionId(), testsRequestModel.getResponse(), username);
                })
                .flatMap(t -> userTestRepository.updateAndGet(username + "_" + t.toString())
                        .flatMap(e -> questionRepository.findNextQuestion(t.toString(), e.getIndex()))
                        .map(e -> new QuestionResponseModel().setUsername(username).setId(e.getId()).setItem1(e.getItem1()).setItem2(e.getItem2()))
                )
                .doOnNext(e -> sink.next(e))
                .thenReturn(Boolean.TRUE);
    }

    public Flux<QuestionResponseModel> getQuestions(String username) {
        return emitterProcessor.publishOn(Schedulers.single())
                .filter(e -> username.equals(e.getUsername()));
    }


}
