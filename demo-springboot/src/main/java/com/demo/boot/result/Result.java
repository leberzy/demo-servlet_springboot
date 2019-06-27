package com.demo.boot.result;

import lombok.Data;

@Data
public class Result {
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;
    private int code;
    private String msg;

    public Result() {
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result getResult(int code, String msg) {
        return new Result(code, msg);
    }

    public static Result getResult(int code) {
        return new Result(code, null);
    }

}
