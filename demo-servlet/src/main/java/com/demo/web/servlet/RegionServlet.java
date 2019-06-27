package com.demo.web.servlet;

import com.alibaba.fastjson.JSON;
import com.demo.pojo.City;
import com.demo.pojo.Province;
import com.demo.result.DataResult;
import com.demo.service.RegionService;
import com.demo.service.impl.RegionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RegionServlet extends BaseServlet {

    public void getProvinces(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        RegionService regionService = new RegionServiceImpl();
        List<Province> allProvince = regionService.getAllProvince();
        DataResult dataResult = DataResult.getDataResult(allProvince);
        String json = JSON.toJSONString(dataResult);
        resp.getWriter().write(json);
        resp.flushBuffer();
    }

    @SuppressWarnings("all")
    public void getCitys(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type","application/json;charset=UTF-8");
        String pid = req.getParameter("pid");
        System.out.println(pid);
        if (Objects.isNull(pid)) {
            return;
        }
        List<City> citys = new RegionServiceImpl().getCitysByProvinceId(Integer.valueOf(pid));
        DataResult dataResult = DataResult.getDataResult(citys);
        String json = JSON.toJSONString(dataResult);
        resp.getWriter().write(json);
        resp.flushBuffer();
    }
}
