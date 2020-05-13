package com.chooseone.controller;

import com.chooseone.model.response.BaseResponse;
import com.chooseone.model.response.PairsResponseModel;
import com.chooseone.security.jwt.JWTUtil;
import com.chooseone.service.PairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pairs")
@RequiredArgsConstructor
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

    @GetMapping(value = "/get-pairs", produces="text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<PairsResponseModel> getPairs(Authentication authentication) {
        return pairService.getPairs(JWTUtil.getPrinciple(authentication).getUsername());
    }

    @GetMapping(value = "/get-pair/{pairsId}", produces="text/event-stream")
    @PreAuthorize("hasRole('USER')")
    public Flux<PairsResponseModel> getPair(@PathVariable("pairsId") String pairsId, Authentication authentication) {

        return pairService.getPairs(JWTUtil.getPrinciple(authentication).getUsername());
    }


}
