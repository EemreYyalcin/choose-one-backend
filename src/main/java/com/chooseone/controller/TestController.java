package com.chooseone.controller;

import com.chooseone.model.request.TestsRequestModel;
import com.chooseone.model.response.QuestionResponseModel;
import com.chooseone.model.response.TestsResponseModel;
import com.chooseone.model.response.UserTestsResponseModel;
import com.chooseone.security.jwt.JWTUtil;
import com.chooseone.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final TestService testService;

    @GetMapping(value = "/get-all-tests", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<TestsResponseModel> getAllTests() {
        return testService.getAllTest().map(e -> new TestsResponseModel().setCategory(e.getCategory()).setType(e.getType()));
    }


    @GetMapping(value = "/get-my-tests", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<UserTestsResponseModel> getTests(Authentication authentication) {
        return testService.getAllUserTests(JWTUtil.getPrinciple(authentication).getUsername())
                .flatMap(e -> Mono.just(e.getTests().stream().map(q -> new UserTestsResponseModel()
                        .setTest(new TestsResponseModel()
                                .setCategory(q.getTestDocument().getCategory())
                                .setType(q.getTestDocument().getType()))).collect(Collectors.toList())))
                .flatMapMany(Flux::fromIterable);
    }

    @PostMapping(value = "/sent-questions", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Mono<Boolean> startTests(@RequestBody @Valid TestsRequestModel testsRequestModel, Authentication authentication) {
        return testService.sentActiveTests(testsRequestModel, JWTUtil.getPrinciple(authentication).getUsername());
    }

    @GetMapping(value = "/sent-answer", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<QuestionResponseModel> getQuestions(Authentication authentication) {
        return testService.getQuestions(JWTUtil.getPrinciple(authentication).getUsername());
    }







}
