package com.demo.boot.mapper;

import com.demo.boot.pojo.City;
import com.demo.boot.pojo.Province;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegionMapper {

    List<Province> selectAllProvince();

    List<City> getCityByProvinceId(@Param("province_id") Integer pid);


}
