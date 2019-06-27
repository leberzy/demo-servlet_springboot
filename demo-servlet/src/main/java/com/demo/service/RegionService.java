package com.demo.service;

import com.demo.pojo.City;
import com.demo.pojo.Province;

import java.util.List;

public interface RegionService {

    List<Province> getAllProvince();

    List<City> getCitysByProvinceId(Integer pid);
}
