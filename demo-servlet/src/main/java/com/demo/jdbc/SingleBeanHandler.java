package com.demo.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SingleBeanHandler<T> extends BaseResultHandler<T> {

    protected Class< ? extends T> type;

    public SingleBeanHandler(Class<? extends T> type) {
        this.type = type;
    }

    @Override
    public T handle(ResultSet resultSet) throws IllegalAccessException, InstantiationException, SQLException {
        if (!resultSet.next()) {
            return null;
        }
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        if (columnCount == 1) {
            T value = (T) getValue(resultSet, md.getColumnName(1), type);
            return  value;
        }
        T instance = type.newInstance();
        populate(resultSet, instance, md, columnCount);
        return instance;
    }

    @SuppressWarnings("all")
    void populate(ResultSet resultSet, T instance, ResultSetMetaData md, int columnCount) throws SQLException, IllegalAccessException {
        for (int i = 1; i <= columnCount; i++) {
            String columnName = md.getColumnName(i);
            Field field;
            try {
                field = type.getDeclaredField(columnName);
            } catch (NoSuchFieldException e) {
                continue;
            }
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            Object value = getValue(resultSet, columnName, fieldType);
            field.set(instance, value);
            field.setAccessible(false);
        }
    }



}
