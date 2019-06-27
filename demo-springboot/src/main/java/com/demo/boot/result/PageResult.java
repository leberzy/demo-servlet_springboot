package com.demo.boot.result;


import lombok.Getter;

import java.util.List;

@Getter
public class PageResult<T> extends DataResult<T> {

    private Integer total;
    private Integer page_size;

    public PageResult(int code, String msg, T data, Integer total, Integer page_size) {
        super(code, msg, data);
        this.total = total;
        this.page_size = page_size;
    }

    public static <T> PageResult getPageResult(List<T> list, Integer total, Integer page_size) {
        return new PageResult(Result.SUCCESS, "", list, total, page_size);
    }

    public static <T> PageResult getPageResult(int code, String msg, int page_size) {
        return new PageResult(code, msg, null, null, page_size);
    }
}
