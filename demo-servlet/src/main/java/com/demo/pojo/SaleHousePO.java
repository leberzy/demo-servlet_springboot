package com.demo.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SaleHousePO {

    //    SALE_ID	int	房源ID，自增序列
    private Integer sale_id;
    //    COMP_ID	int	公司ID
    private Integer comp_id = 1;
    //    CITY_ID	int	城市ID
    private Integer city_id;
    //    DEPT_ID	int	分店ID
    private Integer dept_id = 1;
    //    CREATION_TIME	datetime	登记时间
    private Date creation_time;
    private Date update_time;
    //    SALE_NO	varchar	系统编码
    private String sale_no = "so_123456";
    //    SALE_USEAGE	tinyint	房屋用途，DD:HOUSE_USEAGE
    private Short sale_useage = 1;
    //    SALE_AREA	numeric	建筑面积
    private Double sale_area;
    //    SALE_TOTAL_PRICE	numeric	售价
    private Double sale_total_price;
    //    SALE_SOURCE	tinyint	信息来源，DD:HOUSE_SOURCE
    private Short sale_source = 1;
    //    SALE_EXPLRTH	bit	房勘，1=已勘0=未勘
    private Byte sale_explrth = 1;
    //    SALE_CONSIGN	tinyint	委托，DD: HOUSE_CONSIGN
    private Short sale_consign = 1;
    //    SALE_MAP	tinyint	图片张数，默认=0
    private Short sale_map = 0;
    //    SALE_LEVEL	tinyint	级别，DD: HOUSE_LEVEL
    private Short sale_level = 1;
    //    PLATE_TYPE	tinyint	盘别，DD：PLATE_TYPE
    private Short plate_type = 1;
    //    SALE_STATUS	tinyint	房屋状态，DD：HOUSE_STATUS
    private Short sale_status = 1;
    //    SHARE_FLAG	bit	精英版登记，1=是0=否
    private Byte share_flag = 1;
    //    RED_FLAG	bit	红色警示，1=是0=否（房源是房堪，客源是带看）
    private Byte red_flag = 0;
    //    FROM_PUBLIC	bit	是否转自抢盘，1=是0=否
    private Byte from_public = 1;
    //    sale_id_old	int	老版center库对应的SALE_ID，Def: 0
    private Integer sale_id_old = 0;
    //    HOUSE_BARGAIN	bit	0:未议价1:已议价，DEF:0
    private Byte house_bargain = 1;
    //    PANORAMA_MAP	int	720相机图片有多少张
    private Integer panorama_map = 0;
    //    YOUYOU_DEAL	tinyint	优优排除成交的推荐数据，0：未成交、1：成交 - 陈文超-2016-10-01
    private Short youyou_deal = 0;
    //    IS_SALE_LEASE	tinyint	是否是租售房源，1=是，默认=0=否
    private Short is_sale_lease = 1;
    //    HOUSE_SITUATION	tinyint	房屋现状，DD：HOUSE_SITUATION
    private Short house_situation = 1;
    //    OLD_TRUE_FLAG	tinyint	老的真房源标签
    private Short old_true_flag = 0;
    //面积 SALE_INNERAREA

    //  ------------------------------
    private Double sale_innerarea;
    //房屋朝向 SALE_DIRECT
    private Byte sale_direct;
    //几室 SALE_HALL
    private Byte sale_room;
    private Byte sale_hall;
    //几卫
    private Byte sale_wei;
    private String sale_subject;
    private String build_name;

}
