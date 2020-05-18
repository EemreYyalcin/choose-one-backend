package com.chooseone.service;

import com.chooseone.data.redis.model.Pair;
import com.chooseone.data.redis.model.test.QuestionDocument;
import com.chooseone.data.redis.model.test.TestDocument;
import com.chooseone.data.redis.model.test.UserTestDocument;
import com.chooseone.data.redis.model.test.UserTestInfo;
import com.chooseone.data.redis.repository.AnswersRepository;
import com.chooseone.data.redis.repository.QuestionRepository;
import com.chooseone.data.redis.repository.TestRepository;
import com.chooseone.data.redis.repository.UserTestRepository;
import com.chooseone.model.request.TestsRequestModel;
import com.chooseone.model.response.QuestionResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserTestRepository userTestRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AnswersRepository answersRepository;

    private EmitterProcessor<QuestionResponseModel> emitterProcessor;
    private FluxSink<QuestionResponseModel> sink;

    @PostConstruct
    public void setup(){
        emitterProcessor = EmitterProcessor.create(10);
        sink = emitterProcessor.sink();
    }

    public Mono<UserTestDocument> getAllUserTests(String username) {
        return userTestRepository.findAllNewestTests(username);
    }

    public Flux<TestDocument> getAllTest() {
        return testRepository.getAll();
    }

    public Mono<Boolean> sentActiveTests(TestsRequestModel testsRequestModel, String username) {
        return testRepository.getOne(new TestDocument(testsRequestModel.getType(), testsRequestModel.getCategory()).toString())
                .doOnNext(t -> {
                    if (ObjectUtils.isEmpty(testsRequestModel.getQuestionId()) || testsRequestModel.getResponse() == 99){
                        return;
                    }
                    answersRepository.findAndUpdate( "answer_" + t.toString(), username, testsRequestModel.getResponse());
                })
                .flatMap(t -> userTestRepository.findAllNewestTests(username)
                        .map(e -> e.getTests().stream()
                                .filter(q -> q.getTestDocument().getCategory().equals(t.getCategory()) && q.getTestDocument().getType().equals(t.getType())).findFirst())
                        .flatMap(e -> questionRepository.findNextQuestion(t.toString(), e.get().getIndex()))
                        .map(e -> new QuestionResponseModel().setItem1(e.getItem1()).setItem2(e.getItem2()))
                        .switchIfEmpty(Mono.defer(() -> {
                            UserTestInfo userTestInfo = new UserTestInfo().setTestDocument(t).setLastUpdatedDate(LocalDateTime.now());
                            UserTestDocument userTestDocument = new UserTestDocument();
                            userTestDocument.getTests().add(userTestInfo);
                            userTestRepository.save(username, userTestDocument);
                            return questionRepository.findNextQuestion(t.toString(), userTestInfo.getIndex()).map(e -> new QuestionResponseModel().setItem1(e.getItem1()).setItem2(e.getItem2()));
                        }))
                )
                .doOnNext(e -> sink.next(e))
                .thenReturn(Boolean.TRUE);
    }

    public Flux<QuestionResponseModel> getQuestions(String username){
        return emitterProcessor.publishOn(Schedulers.single())
                .filter(e -> username.equals(e.getUsername()));
    }


}
