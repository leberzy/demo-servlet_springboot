package com.demo.boot.pojo;

public class SaleHouseParam {

    private Integer page_size = 1;
    private Integer page_num = 5;

    private Double max_price = 100000000D;
    private Double min_price = 0D;

    private Integer city_id;
    private Integer sale_room;
//    private Integer reg_id;


    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getPage_num() {
        return page_num;
    }

    public void setPage_num(Integer page_num) {
        this.page_num = page_num;
    }

    public Double getMax_price() {
        return max_price;
    }

    public void setMax_price(Double max_price) {
        this.max_price = max_price;
    }

    public Double getMin_price() {
        return min_price;
    }

    public void setMin_price(Double min_price) {
        this.min_price = min_price;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public Integer getSale_room() {
        if (sale_room <= 0) {
            return null;
        }
        return sale_room;
    }

    public void setSale_room(Integer sale_room) {
        this.sale_room = sale_room;
    }

    public int getOffset() {
        return (page_num - 1) * page_size;
    }
}
