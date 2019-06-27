package com.demo.test;

import com.demo.pojo.SaleHouse;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Demo {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        SaleHouse saleHouse = new SaleHouse();
        Map<String,String> map =  new HashMap<>();
        map.put("sale_id", "1");
        map.put("sale_innerarea", "22.22");
        map.put("sale_wei", "2");
        map.put("update_time", "2019-10-10");
        BeanUtils.populate(saleHouse, map);
        System.out.println(saleHouse);
    }
}
