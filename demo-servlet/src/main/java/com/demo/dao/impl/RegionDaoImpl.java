package com.demo.dao.impl;

import com.demo.dao.RegionDao;
import com.demo.jdbc.ConnHolder;
import com.demo.jdbc.JdbcUtil;
import com.demo.jdbc.ListBeanHandler;
import com.demo.pojo.City;
import com.demo.pojo.Province;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RegionDaoImpl implements RegionDao {

    @SuppressWarnings("all")
    public List<Province> selectAllProvince() throws SQLException {
        Connection conn = ConnHolder.getConnection();
        String sql = "select province_id,province_name from FUN_PROVINCE;";
        List<Province> list = JdbcUtil.query(conn, sql, new ListBeanHandler<>(Province.class));
        return list;
    }

    @SuppressWarnings("all")
    public List<City> getCitysByProvinceId(Integer pid) throws SQLException {
        Connection conn = ConnHolder.getConnection();
        String sql = "select city_id,city_name,province_id from fun_city where PROVINCE_ID = ?";
        List<City> list = JdbcUtil.query(conn, sql, new ListBeanHandler<>(City.class), pid);
        return list;
    }


}
