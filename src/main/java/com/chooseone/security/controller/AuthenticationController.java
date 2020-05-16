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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

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

    @RequestMapping(value = "/isLogin", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<BaseResponse<Boolean>>> isLogIn(){
        return Mono.just(ResponseEntity.ok(BaseResponse.createSuccessResponse(true)));
    }
}