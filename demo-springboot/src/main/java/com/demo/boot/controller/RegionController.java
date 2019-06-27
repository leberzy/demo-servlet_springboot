package com.demo.boot.controller;

import com.demo.boot.pojo.City;
import com.demo.boot.pojo.Province;
import com.demo.boot.result.DataResult;
import com.demo.boot.result.Result;
import com.demo.boot.service.impl.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/region/")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping("/provinces")
    public Result getProvinces() throws IOException {
        List<Province> allProvince = regionService.getAllProvince();
        return DataResult.getDataResult(allProvince);
    }
    @GetMapping("/citys/{pid}")
    public Result getCitys(@PathVariable("pid") String pid) throws ServletException, IOException {
        if (Objects.isNull(pid)) {
            return Result.getResult(Result.FAIL,"procince id not be null.");
        }
        List<City> citys = regionService.getCityByProvinceId(Integer.valueOf(pid));
        return DataResult.getDataResult(citys);
    }
}
