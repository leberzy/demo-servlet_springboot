package com.demo.boot.exception;

public class ParamErrorException extends RuntimeException {
    public ParamErrorException(String msg) {
        super(msg);
    }
}
