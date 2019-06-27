package com.demo.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

public abstract class BaseResultHandler<T> implements ResultHandler<T> {


    protected Object getValue(ResultSet resultSet, String columnName, Class<?> fieldType) throws SQLException {
        Object value = null;
        if (fieldType == int.class || fieldType == Integer.class) {
            value = resultSet.getInt(columnName);
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            value = resultSet.getByte(columnName);
        } else if (fieldType == short.class || fieldType == Short.class) {
            value = resultSet.getShort(columnName);
        } else if (fieldType == long.class || fieldType == Long.class) {
            value = resultSet.getLong(columnName);
        } else if (fieldType == float.class || fieldType == Float.class) {
            value = resultSet.getFloat(columnName);
        } else if (fieldType == double.class || fieldType == Double.class) {
            value = resultSet.getDouble(columnName);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            value = resultSet.getBoolean(columnName);
        } else if (fieldType == String.class) {
            value = resultSet.getString(columnName);
        } else if (fieldType == Date.class || fieldType == java.sql.Date.class) {
            value =new Date(resultSet.getDate(columnName).getTime());
        } else {
            value = resultSet.getObject(columnName);
        }
        return value;
    }
}
