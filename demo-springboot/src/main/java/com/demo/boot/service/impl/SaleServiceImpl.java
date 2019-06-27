package com.demo.boot.service.impl;


import com.demo.boot.exception.ParamErrorException;
import com.demo.boot.mapper.SaleHouseMapper;
import com.demo.boot.pojo.SaleHouse;
import com.demo.boot.pojo.po.SaleHousePO;
import com.demo.boot.pojo.SaleHouseParam;
import com.demo.boot.result.PageResult;
import com.demo.boot.service.SaleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SaleServiceImpl implements SaleService, InitializingBean {

    final AtomicInteger idGenerator = new AtomicInteger();


    @Autowired
    private SaleHouseMapper saleHouseMapper;

    public void afterPropertiesSet() {
        Integer maxSaleId = saleHouseMapper.getMaxSaleId();
        if (Objects.isNull(maxSaleId)) {
            maxSaleId = 0;
        }
        idGenerator.set(maxSaleId);
    }

    public boolean addSaleHouse(SaleHouse house) {
        SaleHousePO saleHousePO = new SaleHousePO();

        BeanUtils.copyProperties(house, saleHousePO);
        saleHousePO.setSale_id(idGenerator.incrementAndGet());
        saleHousePO.setCreation_time(new Date());
        saleHousePO.setUpdate_time(new Date());
        saleHousePO.setSale_area(saleHousePO.getSale_innerarea());
        return 1 == saleHouseMapper.insertRecord(saleHousePO);
    }

    public SaleHouse getHouseDetailById(Integer id){
        SaleHouse saleHouse = saleHouseMapper.selectById(id);
        return saleHouse;
    }

    public boolean updateHouseDetailById(SaleHouse house) {
        if (Objects.isNull(house) || Objects.isNull(house.getSale_id())) {
            throw new ParamErrorException("param not be null, specially sale should not be null.");
        }
        return 1 == saleHouseMapper.updateById(house);
    }

    public boolean delHouseById(Integer id) {

        return saleHouseMapper.delById(id) == 1;
    }

    @SuppressWarnings("all")
    public PageResult<SaleHouse> getSaleHouseByPage(SaleHouseParam param) {

        Integer totalRecord = saleHouseMapper.selectTotalRecord(param);

        List<SaleHouse> list = saleHouseMapper.selectByPage(param);
        return PageResult.<SaleHouse>getPageResult(list, totalRecord, param.getPage_size());
    }

    @Override
    public List<SaleHouse> getExportList(SaleHouseParam param) {
        return saleHouseMapper.selectForExport(param);
    }
}
