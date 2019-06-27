package com.demo.boot.controller;

import com.demo.boot.exception.ParamErrorException;
import com.demo.boot.pojo.SaleHouse;
import com.demo.boot.pojo.SaleHouseParam;
import com.demo.boot.result.DataResult;
import com.demo.boot.result.PageResult;
import com.demo.boot.result.Result;
import com.demo.boot.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/sale/house")
public class SaleHouseController {


    @Autowired
    private SaleService saleService;

    @PostMapping("/list")
    public Result getSaleHouseByPage(@RequestBody SaleHouseParam param) {
        PageResult<SaleHouse> housePageResult = saleService.getSaleHouseByPage(param);
        return housePageResult;
    }

    @PostMapping("/source")
    public Result addSaleHouse(@RequestBody SaleHouse param) {
        if (Objects.isNull(param.getSale_id())) {
            if (saleService.addSaleHouse(param)) {
                return Result.getResult(Result.SUCCESS, "success");
            }
        } else {
            if (saleService.updateHouseDetailById(param)) {
                return Result.getResult(Result.SUCCESS, "success");
            }
        }
        return Result.getResult(Result.FAIL, "add or update error.");
    }

    @GetMapping("/{saleId}")
    public Result getBySaleId(@PathVariable("saleId") Integer saleId) {
        if (Objects.isNull(saleId)) {
            throw new ParamErrorException("sale id not be null.");
        }
            SaleHouse house = saleService.getHouseDetailById(saleId);
            return DataResult.getDataResult(house);
    }

    @PutMapping("/{saleId}")
    public Result editSaleHouseInfo(@PathVariable("saleId") Integer saleId, @RequestBody SaleHouse saleHouse) {
        saleHouse.setSale_id(saleId);
        if (saleService.updateHouseDetailById(saleHouse)) {
            return Result.getResult(Result.SUCCESS, "success");
        }
        return Result.getResult(Result.FAIL, "update exceptions");
    }

    @DeleteMapping("/{saleId}")
    public Result delById(@PathVariable("saleId") Integer saleId) {
        if (Objects.isNull(saleId)) {
            throw new ParamErrorException("sale id not be null.");
        }
        if (saleService.delHouseById(saleId)) {
            return Result.getResult(Result.SUCCESS, "success");
        }
        return Result.getResult(Result.FAIL, "delete exceptions");
    }
}
