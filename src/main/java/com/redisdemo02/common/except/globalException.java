package com.redisdemo02.common.except;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.redisdemo02.Result.Result;

@RestControllerAdvice
public class globalException {
    
    @ExceptionHandler
    public Result handlerException(Exception e){
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

}
