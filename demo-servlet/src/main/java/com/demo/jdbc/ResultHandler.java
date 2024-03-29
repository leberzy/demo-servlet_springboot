package com.demo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler<T> {

    T handle(ResultSet resultSet) throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException;
}
