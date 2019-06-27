package com.demo.dao.impl;

import com.demo.dao.SaleDao;
import com.demo.jdbc.ConnHolder;
import com.demo.jdbc.JdbcUtil;
import com.demo.jdbc.ListBeanHandler;
import com.demo.jdbc.SingleBeanHandler;
import com.demo.pojo.SaleHouse;
import com.demo.pojo.SaleHousePO;
import com.demo.pojo.SaleHouseParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SaleDaoImpl implements SaleDao {
    @SuppressWarnings("all")
    public int insertRecord(SaleHousePO house) throws SQLException {

        Connection conn = ConnHolder.getConnection();
        String sql = "insert into FUN_SALE(" +
                "SALE_ID,COMP_ID,CITY_ID,DEPT_ID,CREATION_TIME,SALE_NO,SALE_USEAGE," +
                "SALE_AREA,SALE_TOTAL_PRICE,SALE_SOURCE,SALE_EXPLRTH,SALE_CONSIGN,SALE_MAP,SALE_LEVEL," +
                "PLATE_TYPE,SALE_STATUS,SHARE_FLAG,RED_FLAG,FROM_PUBLIC,SALE_ID_OLD,HOUSE_BARGAIN," +
                " PANORAMA_MAP,YOUYOU_DEAL,IS_SALE_LEASE,HOUSE_SITUATION,OLD_TRUE_FLAG," +
                " SALE_INNERAREA,SALE_DIRECT,SALE_ROOM,SALE_HALL,SALE_WEI,SALE_SUBJECT,BUILD_NAME,UPDATE_TIME)" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        //设值
        Object[] params = getParams(house);
        return JdbcUtil.update(conn, sql, params);
    }

    @SuppressWarnings("all")
    public SaleHouse selectById(int id) throws SQLException {
        String sql = "select sale_id,sale_subject,build_name,city_id,update_time,sale_status,sale_total_price,sale_innerarea,sale_direct,sale_room,sale_hall,sale_wei from FUN_SALE where SALE_ID = ?";
        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.query(conn, sql, new SingleBeanHandler<>(SaleHouse.class), id);
    }

    @SuppressWarnings("all")
    public int deleteById(int id) throws SQLException {
//        String sql = "delete from FUN_SALE WHERE SALE_ID=?";
        String sql = "update FUN_SALE set sale_status =100 where SALE_ID=?";
        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.update(conn, sql, id);
    }

    @SuppressWarnings("all")
    public int updateById(SaleHouse house) throws SQLException {
        //拼凑Sql语句
        String sql = "update FUN_SALE set ";
        Map<String, Object> map = new LinkedHashMap<>();
        checkParam(house, map);
        StringBuffer buff = new StringBuffer(sql);
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        ArrayList<Object> objects = new ArrayList<>();
        //修改的字段
        for (Map.Entry<String, Object> entry : entries) {
            buff.append("  ").append(entry.getKey()).append("=?,");
            objects.add(entry.getValue());
        }
        objects.add(house.getSale_id());
        sql = buff.substring(0, buff.length() - 1);
        sql += " where SALE_ID=?";
        //连接执行
        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.update(conn, sql, objects.toArray());
    }

    @SuppressWarnings("all")
    public List<SaleHouse> selectAllList(SaleHouseParam param) throws SQLException {

        String sql = "select sale_id,sale_subject,build_name,city_id,update_time,sale_status" +
                "        ,sale_total_price,sale_innerarea,sale_direct,sale_room,sale_wei " +
                "        from FUN_SALE " +
                "        where SALE_STATUS != 100" +
                "        and city_id = ?" +
                "        and sale_total_price <= ?" +
                "        and sale_total_price >= ?" +
                "        and sale_room = ?";
        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.query(conn,sql, new ListBeanHandler<>(SaleHouse.class),
                param.getCity_id(),param.getMax_price(),param.getMin_price(),param.getSale_room());
    }

    @SuppressWarnings("all")
    public List<SaleHouse> selectByPage(SaleHouseParam param) throws SQLException {
        String sql = "select top "+param.getPage_size()+" sale_id,sale_subject,build_name,city_id,update_time,sale_status,sale_total_price,sale_innerarea,sale_direct,sale_room,sale_hall,sale_wei " +
                " from (select row_number() over(order by sale_id asc) as rownumber,sale_id,sale_subject,build_name,city_id,update_time,sale_status,sale_total_price,sale_innerarea,sale_direct,sale_room,sale_hall,sale_wei" +
                " from fun_sale  where  sale_status!=100 and city_id=? and sale_total_price<=? and sale_total_price>=? and sale_room=? ) temp where rownumber > ?;";

        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.query(conn, sql, new ListBeanHandler<>(SaleHouse.class),
                param.getCity_id(), param.getMax_price(), param.getMin_price()
                ,param.getSale_room(),param.getOffset());
    }

    /**
     * 获取当前最大ID值
     *
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("all")
    public int getMaxId() throws SQLException {
        String sql = "select max(SALE_ID) as id from FUN_SALE;";
        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.query(conn, sql, new SingleBeanHandler<>(int.class));
    }

    @SuppressWarnings("all")
    public int selectTotalRecord(SaleHouseParam param) throws SQLException {
        String sql = "select count(SALE_ID) " +
                " from FUN_SALE where SALE_STATUS != 100 " +
                " and city_id = ? " +
                " and sale_total_price <= ? " +
                " and sale_total_price >= ? " +
                " and sale_room = ? ";
        Connection conn = ConnHolder.getConnection();
        return JdbcUtil.query(conn, sql, new SingleBeanHandler<>(Integer.class),
                param.getCity_id(), param.getMax_price(), param.getMin_price(),
                param.getSale_room());
    }

    //私有方法
//-------------------------------------------------------------------------------------------

    //update 修改的字段
    private void checkParam(SaleHouse house, Map<String, Object> map) {
        if (Objects.nonNull(house.getSale_subject())) {
            map.put("SALE_SUBJECT", house.getSale_subject());
        }
        if (Objects.nonNull(house.getBuild_name())) {
            map.put("BUILD_NAME", house.getBuild_name());
        }
        if (Objects.nonNull(house.getCity_id())) {
            map.put("CITY_ID", house.getCity_id());
        }
        if (Objects.nonNull(house.getUpdate_time())) {
            map.put("UPDATE_TIME", new java.sql.Date(house.getUpdate_time().getTime()));
        }
        if (Objects.nonNull(house.getSale_status())) {
            map.put("SALE_STATUS", house.getSale_status());
        }
        if (Objects.nonNull(house.getSale_total_price())) {
            map.put("SALE_TOTAL_PRICE", house.getSale_total_price());
        }
        if (Objects.nonNull(house.getSale_innerarea())) {
            map.put("SALE_INNERAREA", house.getSale_innerarea());
        }
        if (Objects.nonNull(house.getSale_direct())) {
            map.put("SALE_DIRECT", house.getSale_direct());
        }
        if (Objects.nonNull(house.getSale_wei())) {
            map.put("SALE_ROOM", house.getSale_room());
        }
        if (Objects.nonNull(house.getSale_wei())) {
            map.put("SALE_HALL", house.getSale_hall());
        }
        if (Objects.nonNull(house.getSale_wei())) {
            map.put("SALE_WEI", house.getSale_wei());
        }
    }

    /**
     * @param house
     * @throws SQLException
     */
    private Object[] getParams(SaleHousePO house){
        //。。
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(house.getSale_id());
        objects.add(house.getComp_id());
        objects.add(house.getCity_id());
        objects.add(house.getDept_id());
        objects.add(new java.sql.Date(house.getCreation_time().getTime()));
        objects.add(house.getSale_no());
        objects.add(house.getSale_useage());
        objects.add(house.getSale_area());
        objects.add(house.getSale_total_price());
        objects.add(house.getSale_source());
        objects.add(house.getSale_explrth());
        objects.add( house.getSale_consign());
        objects.add(house.getSale_map());
        objects.add(house.getSale_level());
        objects.add(house.getPlate_type());
        objects.add(house.getSale_status());
        objects.add(house.getShare_flag());
        objects.add(house.getRed_flag());
        objects.add(house.getFrom_public());
        objects.add(house.getSale_id_old());
        objects.add(house.getHouse_bargain());
        objects.add(house.getPanorama_map());
        objects.add(house.getYouyou_deal());
        objects.add(house.getIs_sale_lease());
        objects.add(house.getHouse_situation());
        objects.add(house.getOld_true_flag());
        objects.add(house.getSale_innerarea());
        objects.add(house.getSale_direct());
        objects.add(house.getSale_room());
        objects.add(house.getSale_hall());
        objects.add(house.getSale_wei());
        objects.add(house.getSale_subject());
        objects.add(house.getBuild_name());
        objects.add(new java.sql.Date(house.getUpdate_time().getTime()));
        return objects.toArray();
    }

    //------------------------------------------------------------------------

    /**
     * 映射每行数据
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private SaleHouse handlerSingleBean(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        Map<String, String> map = new HashMap<>();
        SaleHouse saleHouse = new SaleHouse();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            String val = resultSet.getString(columnName);
            map.put(columnName.toLowerCase(), val);
        }
        //注册一个日期转换器
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                Date date1 = null;
                if (value instanceof String) {
                    String date = (String) value;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date1 = sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return date1;
            }
        }, Date.class);
        try {
            //内省设值
            BeanUtils.populate(saleHouse, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.clear();
        return saleHouse;
    }
}
