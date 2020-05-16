package com.chooseone.service;

import com.chooseone.data.redis.model.Imdb;
import com.chooseone.data.redis.model.KeyList;
import com.chooseone.data.redis.model.Pair;
import com.chooseone.data.redis.model.Pairs;
import com.chooseone.data.redis.repository.PairRepository;
import com.chooseone.data.redis.repository.PairsRepository;
import com.chooseone.data.redis.repository.UserPairsRepository;
import com.chooseone.model.request.PairRequestModel;
import com.chooseone.model.response.PairResponseModel;
import com.chooseone.model.response.PairsResponseModel;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PairService {

    private final ImdbService imdbService;
    private final PairsRepository pairsRepository;
    private final PairRepository pairRepository;
    private final UserPairsRepository userPairsRepository;

    private EmitterProcessor<Pair> emitterProcessor;
    private FluxSink<Pair> sink;

    @PostConstruct
    public void setup(){
        emitterProcessor = EmitterProcessor.create(10);
        sink = emitterProcessor.sink();
    }


    private int testSize = 5;

    public String createPairs(String username) {
        try {
            List<Imdb> randomItems = imdbService.getRandomItems(testSize * 2);
            Pairs pairs = new Pairs().setClient1(username);
            String keyPairs = UUID.randomUUID().toString().replaceAll("-", "");
            KeyList pairItems = new KeyList();
            String key;
            for (int i = 0; i < randomItems.size(); i += 2) {
                Pair pair = new Pair().setClient1(username)
                        .setItem1(randomItems.get(i).getPoster())
                        .setItem2(randomItems.get(i + 1).getPoster())
                        .setParentPairs(keyPairs);
                key = UUID.randomUUID().toString().replaceAll("-", "");
                pairItems.getKeys().add(key);
                pairRepository.save(pair, key);
            }
            pairsRepository.save(pairs.setPairItems(pairItems), keyPairs);
            userPairsRepository.addPairs(username, keyPairs);
            return keyPairs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Flux<PairsResponseModel> getPairs(String username){
        return userPairsRepository.getPairs(username)
                .flatMap(Mono::just)
                .flatMap(e -> Mono.just(e.getKeys().stream().map(pairsKey -> new PairsResponseModel().setHash(pairsKey)).collect(Collectors.toList())))
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Boolean> sentEventPair(PairRequestModel pairRequestModel, String username){
        if (ObjectUtils.isEmpty(pairRequestModel.getPairId())){
            //First Click
            log.info("sentEventPair: first");
            return pairRepository.getPairFromPairs(pairRequestModel.getPairs())
                    .switchIfEmpty(Mono.defer(() -> {
                        Pair pair = new Pair().setResult(true).setParentPairs(pairRequestModel.getPairs()).setClient1(username);
                        sink.next(pair);
                        return Mono.just(pair);
                    }))
                    .doOnNext(e -> sink.next(e))
                    .thenReturn(Boolean.TRUE);
        }

        log.info("sentEventPair: more");
        return pairRepository.updateAndGetNextPair(pairRequestModel.getPairId(), username, pairRequestModel.getClickItem())
                .switchIfEmpty(Mono.defer(() -> {
                    Pair pair = new Pair().setResult(true).setParentPairs(pairRequestModel.getPairs()).setClient1(username);
                    sink.next(pair);
                    return Mono.just(pair);
                }))
                .doOnNext(e -> sink.next(e))
                .thenReturn(Boolean.TRUE);
    }

    public Flux<PairResponseModel> getEventPair(String username, String pairs){
            return emitterProcessor.publishOn(Schedulers.single())
                    .filter(e -> username.equals(e.getClient1()))
                    .filter(e -> pairs.equals(e.getParentPairs()))
                    .map(e -> new PairResponseModel().setResult(e.isResult()).setPairId(e.getId()).setItem1(e.getItem1()).setItem2(e.getItem2()).setRate(pairRepository.getRate(username, 1)));
    }


}
