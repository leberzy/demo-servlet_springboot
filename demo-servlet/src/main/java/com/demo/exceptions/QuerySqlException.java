package com.demo.exceptions;

public class QuerySqlException extends RuntimeException {
    public QuerySqlException(String message) {
        super(message);
    }
}
