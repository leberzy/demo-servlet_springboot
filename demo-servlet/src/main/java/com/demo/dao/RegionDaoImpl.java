package com.demo.dao;

import com.demo.jdbc.ConnHolder;
import com.demo.jdbc.JdbcUtil;
import com.demo.pojo.City;
import com.demo.pojo.Province;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDaoImpl implements RegionDao{

    @SuppressWarnings("all")
    public List<Province> selectAllProvince() throws SQLException {
        Connection conn = ConnHolder.getConnection();
        PreparedStatement prep = conn.prepareStatement("select PROVINCE_ID,PROVINCE_NAME from FUN_PROVINCE;");
        prep.execute();
        ResultSet resultSet = prep.getResultSet();
        List<Province> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Province(resultSet.getInt(1), resultSet.getString(2)));
        }
        return list;
    }

    @SuppressWarnings("all")
    public List<City> getCitysByProvinceId(Integer pid) throws SQLException {
        Connection conn = ConnHolder.getConnection();
        PreparedStatement prep = conn.prepareStatement("select CITY_ID,CITY_NAME from FUN_CITY where PROVINCE_ID = ?");
        prep.setInt(1, pid);
        prep.execute();
        ResultSet resultSet = prep.getResultSet();
        List<City> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new City(resultSet.getInt(1), resultSet.getString(2), pid));
        }
        prep.close();
        return list;
    }


}
