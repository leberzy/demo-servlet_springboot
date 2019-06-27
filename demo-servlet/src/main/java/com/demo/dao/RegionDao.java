package com.demo.dao;

import com.demo.pojo.City;
import com.demo.pojo.Province;

import java.sql.SQLException;
import java.util.List;

public interface RegionDao {

    List<Province> selectAllProvince() throws SQLException;

    List<City> getCitysByProvinceId(Integer pid) throws SQLException;
}
