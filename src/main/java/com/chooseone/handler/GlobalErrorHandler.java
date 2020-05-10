package com.chooseone.handler;

import com.chooseone.model.response.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private ObjectMapper objectMapper;

    public GlobalErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        DataBuffer dataBuffer;
        throwable.printStackTrace();
        if (throwable instanceof IOException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            try {
                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(BaseResponse.createFailureResponse("IO Error Occurred")));
            } catch (JsonProcessingException e) {
                dataBuffer = bufferFactory.wrap("".getBytes());
            }
            serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
        }

        if (throwable instanceof WebExchangeBindException) {
            WebExchangeBindException exception = (WebExchangeBindException) throwable;
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            try {
                BaseResponse<String> resp = BaseResponse.createFailureResponse(exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(",")));
                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(resp));
            } catch (JsonProcessingException e) {
                dataBuffer = bufferFactory.wrap("".getBytes());
            }
            serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
        }

        serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        try {
            dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(BaseResponse.createFailureResponse("Unknown Error")));
        } catch (JsonProcessingException e) {
            dataBuffer = bufferFactory.wrap("Unknown Error".getBytes());
        }
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

}