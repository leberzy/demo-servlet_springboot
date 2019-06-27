package com.demo.test;

import com.demo.dao.impl.SaleDaoImpl;
import com.demo.jdbc.JdbcUtil;
import com.demo.jdbc.ListBeanHandler;
import com.demo.pojo.City;

import java.sql.Connection;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws Exception {

        Connection conn = JdbcUtil.getConnection(10000);
//        String sql = "select sale_id,sale_subject,build_name,city_id,update_time,sale_status,sale_total_price,sale_innerarea,sale_direct,sale_room,sale_hall,sale_wei from fun_sale where sale_id = ?";
//        SaleHouse saleHouse = JdbcUtil.query(conn, sql, new SingleBeanHandler<>(SaleHouse.class), 1231231251);
//        System.out.println(saleHouse);
//        String sql1 = "select city_id,city_name from fun_city where PROVINCE_ID = ?";
//        List<City> query = JdbcUtil.query(conn, sql1, new ListBeanHandler<>(City.class), 1);
//        System.out.println(query);

        int maxId = new SaleDaoImpl().getMaxId();
        System.out.println(maxId);
    }
}
