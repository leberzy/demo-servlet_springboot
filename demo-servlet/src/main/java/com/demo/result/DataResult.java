package com.demo.result;

import lombok.Data;

@Data
public class DataResult<T> extends Result {

    private Object data;

    public DataResult() {
    }

    public DataResult(int code, String msg, T data) {
        super(code,msg);
        this.data = data;
    }

    public static <T> DataResult getDataResult(T data) {
        return new DataResult(0,"ok",data);
    }

    public static <T> DataResult getDataResult(int code, String msg, T data) {
        return new DataResult(code, msg, data);
    }

}
