package com.chooseone.security.controller;

import com.chooseone.data.redis.model.UserInfo;
import com.chooseone.model.response.BaseResponse;
import com.chooseone.security.config.PBKDF2Encoder;
import com.chooseone.security.jwt.JWTUtil;
import com.chooseone.security.model.request.AuthRequest;
import com.chooseone.security.model.request.SignUpRequest;
import com.chooseone.security.model.response.AuthResponse;
import com.chooseone.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationREST {

    private final JWTUtil jwtUtil;

    private final PBKDF2Encoder passwordEncoder;

    private final UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<BaseResponse<AuthResponse>>> login(@RequestBody @Valid AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(userInfo -> passwordEncoder.encode(ar.getPassword()).equals(userInfo.getPassword()))
                .map(userInfo -> ResponseEntity.ok(BaseResponse.createSuccessResponse(new AuthResponse(jwtUtil.generateToken(userInfo), userInfo.getUsername()))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.createFailureResponse(null, "Username not found!")));
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Mono<ResponseEntity<BaseResponse<AuthResponse>>> signUp(@RequestBody SignUpRequest signUpRequest) {
        return userService.saveUser(signUpRequest)
                .filter(userInfo -> !userInfo.isError())
                .map(userInfo -> ResponseEntity.ok(BaseResponse.createSuccessResponse(new AuthResponse(jwtUtil.generateToken(userInfo), userInfo.getUsername()))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.createResponse(null, "User is already defined", 999)));
    }

}