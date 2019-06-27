package com.demo.boot.service;

import com.demo.boot.pojo.SaleHouse;
import com.demo.boot.pojo.SaleHouseParam;
import com.demo.boot.result.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface SaleService {

    boolean addSaleHouse(SaleHouse house);

    SaleHouse getHouseDetailById(Integer id);

    boolean updateHouseDetailById(SaleHouse house);


    boolean delHouseById(Integer id);

    PageResult<SaleHouse> getSaleHouseByPage(SaleHouseParam param);

    List<SaleHouse> getExportList(SaleHouseParam param);
}
