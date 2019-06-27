package com.demo.service.impl;

import com.demo.dao.RegionDaoImpl;
import com.demo.exceptions.QuerySqlException;
import com.demo.jdbc.ConnHolder;
import com.demo.pojo.City;
import com.demo.pojo.Province;
import com.demo.service.RegionService;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class RegionServiceImpl implements RegionService {

    @Override
    public List<Province> getAllProvince() {
        ConnHolder.checkConnection();
        try {
            return new RegionDaoImpl().selectAllProvince();
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        }finally {
            ConnHolder.releaseQuietly();
        }
    }

    @Override
    public List<City> getCitysByProvinceId(Integer pid) {
        ConnHolder.checkConnection();
        try {
            return new RegionDaoImpl().getCitysByProvinceId(pid);
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        }finally {
            ConnHolder.releaseQuietly();
        }
    }
}
