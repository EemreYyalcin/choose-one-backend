package com.chooseone.controller;

import com.chooseone.service.PairService;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/test")
public class TestController {

    EmitterProcessor<String> emitterProcessor = EmitterProcessor.create(1);
    FluxSink<String> skink2;

    public TestController(){
        skink2 = emitterProcessor.sink();
    }

    int count = 0;
    @GetMapping("/send2")
    public void test2() {
        skink2.next("TESTT" + count++);
    }

    @GetMapping(value = "/receive2",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sse2() {
        return emitterProcessor.publishOn(Schedulers.single())
                .map(String::toUpperCase);
    }






}
