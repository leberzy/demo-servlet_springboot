package com.demo.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class JdbcUtil {

    private JdbcUtil() {
    }

    //线程池
    private static final ArrayBlockingQueue<Connection> pool = new ArrayBlockingQueue<>(10);

    //初始化线程池
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            for (int i = 0; i < 10; i++) {
                pool.add(initConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建代理连接
    private static Connection initConnection() throws SQLException {
        final Connection conn = DriverManager
                .getConnection("jdbc:sqlserver://192.168.11.106:33434;database=hft_erpdb_AK  ",
                        "user_fafa", "user_fafa123456");
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //归还至连接池
                        if (method.getName().equals("close")) {
                            pool.add((Connection) proxy);
//                            System.out.println("连接池数量:" + pool.size());
                            return null;
                        } else {
                            return method.invoke(conn, args);
                        }
                    }
                });
    }

    /**
     * 获取连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(long timeout) throws SQLException {
//            return pool.poll(timeout, TimeUnit.MICROSECONDS);
        return DriverManager
                .getConnection("jdbc:sqlserver://192.168.11.106:33434;database=hft_erpdb_AK  ",
                        "user_fafa", "user_fafa123456");
    }

    /**
     * 关闭连接资源
     *
     * @param conn
     * @param prep
     * @param resultSet
     */
    public static void close(Connection conn, PreparedStatement prep, ResultSet resultSet) {
        if (Objects.nonNull(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(prep)) {
            try {
                prep.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(resultSet)) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(PreparedStatement prep, ResultSet resultSet) {
        close(null, prep, resultSet);
    }

    //----------------------------------查询封装--------------------------------
    public static <T> T query(Connection conn, String sql, ResultHandler<T> handler, Object... param) throws SQLException {
        //sql 设值
        PreparedStatement prep = conn.prepareStatement(sql);
        setValue(prep, param);
        // query
        prep.executeQuery();
        //resultSet处理
        ResultSet rs = prep.getResultSet();
        T result = null;
        try {
            result = handler.handle(rs);
        } catch (IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        close(prep, rs);
        return result;
    }


    public static int update(Connection conn, String sql, Object... param) throws SQLException {
        //sql 设值
        PreparedStatement prep = conn.prepareStatement(sql);
        setValue(prep, param);
        // exec
        prep.executeUpdate();
        int updateCount = prep.getUpdateCount();
        close(prep, null);
        return updateCount;
    }

    private static void setValue(PreparedStatement prep, Object[] param) throws SQLException {
        ParameterMetaData pmd = prep.getParameterMetaData();
        int pmdParameterCount = pmd.getParameterCount();
        int paramCount = param == null ? 0 : param.length;
        if (pmdParameterCount != paramCount) {
            throw new SQLException("Wrong number of parameters: expected "
                    + pmdParameterCount + ", was given " + paramCount);
        }
        //do nothing
        if (Objects.isNull(param) || param.length == 0) {
            return;
        }
        //set value
        for (int i = 0; i < param.length; i++) {
            prep.setObject(i + 1, param[i]);
        }
    }

}
