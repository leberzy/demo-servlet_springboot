package com.demo.boot.controller;

import com.demo.boot.exception.ParamErrorException;
import com.demo.boot.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = ParamErrorException.class)
    public Result paramErrorException(ParamErrorException e) {
        log.error("param exceptions.\n", e);
        return Result.getResult(Result.FAIL, e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public Result sQLException(ParamErrorException e) {
        log.error("db error.\n", e);
        return Result.getResult(Result.FAIL, "query sql error");
    }
}
