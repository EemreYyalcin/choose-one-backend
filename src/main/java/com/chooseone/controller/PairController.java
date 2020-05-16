package com.chooseone.controller;

import com.chooseone.model.request.PairGetRequestModel;
import com.chooseone.model.request.PairRequestModel;
import com.chooseone.model.response.BaseResponse;
import com.chooseone.model.response.PairResponseModel;
import com.chooseone.model.response.PairsResponseModel;
import com.chooseone.security.jwt.JWTUtil;
import com.chooseone.service.PairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/pairs")
@RequiredArgsConstructor
@Slf4j
public class PairController {

    private final PairService pairService;

    @GetMapping("/create-pairs")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<BaseResponse<String>>> createPairs(Authentication authentication) {
        String pairsKey = pairService.createPairs(JWTUtil.getPrinciple(authentication).getUsername());
        if (ObjectUtils.isEmpty(pairsKey)) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.createFailureResponse("Exception Occurred")));
        }
        return Mono.just(ResponseEntity.ok(BaseResponse.createSuccessResponse(pairsKey)));
    }

    @GetMapping(value = "/get-pairs", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<PairsResponseModel> getPairs(Authentication authentication) {
        return pairService.getPairs(JWTUtil.getPrinciple(authentication).getUsername());
    }

    @PostMapping(value = "/send-pair", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Mono<Boolean> getPair(@RequestBody PairRequestModel pairRequestModel, Authentication authentication) {
        return pairService.sentEventPair(pairRequestModel, JWTUtil.getPrinciple(authentication).getUsername());
    }

    @GetMapping(value = "/get-pair/{pairs}", produces = "text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<PairResponseModel> getPair(@PathVariable("pairs") String pairs, Authentication authentication) {
        log.info("getPair:" + pairs);
        return pairService.getEventPair(JWTUtil.getPrinciple(authentication).getUsername(), pairs);
    }


}
