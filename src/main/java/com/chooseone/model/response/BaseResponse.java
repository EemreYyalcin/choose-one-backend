package com.chooseone.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BaseResponse<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> BaseResponse<T> createSuccessResponse(T data){
        return createSuccessResponse(data,"Success");
    }

    public static <T> BaseResponse<T> createSuccessResponse(T data, String message){
        return new BaseResponse<T>().setCode(0).setData(data).setMessage(message);
    }


    public static <T> BaseResponse<T> createFailureResponse(T data){
        return createFailureResponse(data, "Failure");
    }

    public static <T> BaseResponse<T> createFailureResponse(T data, String message){
        return new BaseResponse<T>().setCode(100).setData(data).setMessage(message);
    }

    public static <T> BaseResponse<T> createResponse(T data, String message, Integer code){
        return new BaseResponse<T>().setData(data).setMessage(message).setCode(code);
    }











}
