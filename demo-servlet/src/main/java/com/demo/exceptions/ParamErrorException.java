package com.demo.exceptions;

public class ParamErrorException extends RuntimeException {
    public ParamErrorException(String msg) {
        super(msg);
    }
}
