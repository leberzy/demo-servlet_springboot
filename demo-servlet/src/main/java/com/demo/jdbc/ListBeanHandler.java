package com.demo.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListBeanHandler<T> extends BaseResultHandler<List<T>> {

    private Class<? extends T> type;

    public ListBeanHandler(Class<? extends T> type) {
        this.type = type;
    }


    @SuppressWarnings("all")
    @Override
    public List<T> handle(ResultSet resultSet) throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {

        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        List<T> list = new ArrayList<>();
        while (resultSet.next()) {
            //查询一列值
            if (columnCount == 1) {
                T value = (T) getValue(resultSet, md.getColumnName(1), type);
                list.add(value);
                continue;
            }
            //JAVA Bean
            T instance = type.newInstance();
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
            list.add(instance);
        }
        return list;
    }
}
