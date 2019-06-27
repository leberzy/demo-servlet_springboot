package com.demo.pojo;

import lombok.Data;

@Data
public class SaleHouseParam {

    private Integer page_size = 1;
    private Integer page_num = 5;

    private Double max_price = 100000000D;
    private Double min_price = 0D;

    private Integer city_id = 1;
    private Integer sale_room = 2;
//    private Integer reg_id;

    public int getOffset() {
        return (page_num - 1) * page_size;
    }
}
