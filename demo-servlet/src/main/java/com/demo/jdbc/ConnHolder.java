package com.demo.jdbc;

import com.demo.exceptions.PersistentConnectionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class ConnHolder {

    private ConnHolder() {

    }

    private static final ThreadLocal<Connection> tl = new ThreadLocal<>();

    public static Connection getConnection() {
        try {
            if (Objects.isNull(tl.get())) {
                tl.set(JdbcUtil.getConnection(1000));
            }
            return tl.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void releaseQuietly() {
        Connection connection = tl.get();
        if (null != connection) {
            tl.set(null);
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 检查当前线程连接
     */
    public static void checkConnection() {
        if (ConnHolder.getConnection() == null) {
            throw new PersistentConnectionException("persistent layer connection exceptions.");
        }
    }
}
