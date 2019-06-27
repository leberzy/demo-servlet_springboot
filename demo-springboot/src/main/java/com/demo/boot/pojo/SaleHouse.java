package com.demo.boot.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleHouse {

    private Integer sale_id;
    //SALE_SUBJECT	否	varchar	100	允许	否	房源标题
    private String sale_subject;
    //BUILD_NAME	否	varchar	100	允许	否	楼盘名称
    //SALE_TOTAL_PRICE
    private Double sale_total_price;

    private String build_name;
    //更新时间

    //面积 SALE_INNERAREA
    private Double sale_innerarea;
    //房屋朝向 SALE_DIRECT
    private Byte sale_direct;
    //几室 SALE_HALL
    private Byte sale_room;

    private Byte sale_hall;
    //几卫
    private Byte sale_wei;

    private Date update_time;
    //城市
    private Integer city_id;
    //房屋状态
    private Byte sale_status;
//    private Integer reg_id;


}
