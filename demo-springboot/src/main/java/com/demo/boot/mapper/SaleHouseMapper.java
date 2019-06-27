package com.demo.boot.mapper;

import com.demo.boot.pojo.SaleHouse;
import com.demo.boot.pojo.po.SaleHousePO;
import com.demo.boot.pojo.SaleHouseParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SaleHouseMapper {


    int insertRecord0(SaleHouse house);

    int insertRecord(SaleHousePO house);

    SaleHouse selectById(@Param("sale_id") Integer id);

    int updateById(SaleHouse house);

    Integer selectTotalRecord(SaleHouseParam param);

    List<SaleHouse> selectByPage(SaleHouseParam param);

    int delById(@Param("sale_id") Integer id);

    Integer getMaxSaleId();

    List<SaleHouse> selectForExport(SaleHouseParam param);
}
