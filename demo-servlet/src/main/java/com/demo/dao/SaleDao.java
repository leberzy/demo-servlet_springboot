package com.demo.dao;

import com.demo.pojo.SaleHouse;
import com.demo.pojo.SaleHousePO;
import com.demo.pojo.SaleHouseParam;

import java.sql.SQLException;
import java.util.List;

public interface SaleDao {

    int insertRecord(SaleHousePO house) throws SQLException;

    SaleHouse selectById(int id) throws SQLException;


    int updateById(SaleHouse house) throws SQLException;

    List<SaleHouse> selectByPage(SaleHouseParam param) throws SQLException;
    List<SaleHouse> selectAllList(SaleHouseParam param) throws SQLException;

    int deleteById(int id) throws SQLException;

    int getMaxId() throws SQLException;

    int selectTotalRecord(SaleHouseParam param) throws SQLException;
}
