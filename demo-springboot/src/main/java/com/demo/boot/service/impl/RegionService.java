package com.demo.boot.service.impl;


import com.demo.boot.exception.ParamErrorException;
import com.demo.boot.mapper.RegionMapper;
import com.demo.boot.pojo.City;
import com.demo.boot.pojo.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RegionService {

    @Autowired
    private RegionMapper regionMapper;


    public List<Province> getAllProvince() {
        return regionMapper.selectAllProvince();

    }

    public List<City> getCityByProvinceId(Integer pid) {
        return regionMapper.getCityByProvinceId(pid);
    }
}
