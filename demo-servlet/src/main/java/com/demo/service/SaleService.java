package com.demo.service;

import com.demo.pojo.SaleHouse;
import com.demo.pojo.SaleHousePO;
import com.demo.pojo.SaleHouseParam;
import com.demo.result.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface SaleService {

    int addSaleHouse(SaleHousePO house);

    SaleHouse getHouseDetailById(Integer id);

    boolean updateHouseDetailById(SaleHouse house);


    boolean delHouseById(Integer id);

    List<SaleHouse> getAllList(SaleHouseParam param);

    PageResult<SaleHouse> getPageResult(SaleHouseParam param);

}
