package com.demo.dao.impl;

import com.demo.dao.SaleDao;
import com.demo.jdbc.ConnHolder;
import com.demo.jdbc.JdbcUtil;
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
        PreparedStatement prep = conn.prepareStatement(
                "insert into FUN_SALE(" +
                        "SALE_ID,COMP_ID,CITY_ID,DEPT_ID,CREATION_TIME,SALE_NO,SALE_USEAGE," +
                        "SALE_AREA,SALE_TOTAL_PRICE,SALE_SOURCE,SALE_EXPLRTH,SALE_CONSIGN,SALE_MAP,SALE_LEVEL," +
                        "PLATE_TYPE,SALE_STATUS,SHARE_FLAG,RED_FLAG,FROM_PUBLIC,SALE_ID_OLD,HOUSE_BARGAIN," +
                        " PANORAMA_MAP,YOUYOU_DEAL,IS_SALE_LEASE,HOUSE_SITUATION,OLD_TRUE_FLAG," +
                        " SALE_INNERAREA,SALE_DIRECT,SALE_ROOM,SALE_HALL,SALE_WEI,SALE_SUBJECT,BUILD_NAME,UPDATE_TIME)" +
                        "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )");
        //设值
        setParamValue(house, prep);
        prep.execute();
        int largeUpdateCount = prep.getUpdateCount();
        //关闭资源
        JdbcUtil.close(prep, null);
        return largeUpdateCount;
    }

    @SuppressWarnings("all")
    public SaleHouse selectById(int id) throws SQLException {
        String sql = "select SALE_ID,SALE_SUBJECT,BUILD_NAME,CITY_ID,UPDATE_TIME,SALE_STATUS,SALE_TOTAL_PRICE,SALE_INNERAREA,SALE_DIRECT,SALE_ROOM,SALE_HALL,SALE_WEI from FUN_SALE where SALE_ID = ?";
        Connection conn = ConnHolder.getConnection();
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setInt(1, id);
        prep.execute();
        ResultSet resultSet = prep.getResultSet();
        if (resultSet.next()) {
            return handlerSingleBean(resultSet);
        }
        JdbcUtil.close(prep, resultSet);
        return null;
    }

    @SuppressWarnings("all")
    public int deleteById(int id) throws SQLException {
//        String sql = "delete from FUN_SALE WHERE SALE_ID=?";
        String sql = "update FUN_SALE set sale_status =100 where SALE_ID=?";
        Connection conn = ConnHolder.getConnection();
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setObject(1, id);
        prep.execute();
        int updateCount = prep.getUpdateCount();
        JdbcUtil.close(prep, null);
        return updateCount;
    }

    @SuppressWarnings("all")
    public int updateById(SaleHouse house) throws SQLException {
        //SALE_ID,SALE_SUBJECT,BUILD_NAME,CITY_ID,UPDATE_TIME,SALE_STATUS,SALE_TOTAL_PRICE,SALE_INNERAREA,SALE_DIRECT,SALE_HALL,SALE_WEI
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
        PreparedStatement prep = conn.prepareStatement(sql);
        int i = 1;
        for (Object object : objects) {
            prep.setObject(i++, object);
        }
        prep.execute();
        //变更记录数
        int updateCount = prep.getUpdateCount();
        JdbcUtil.close(prep, null);
        return updateCount;
    }

    @SuppressWarnings("all")
    public List<SaleHouse> selectAllList(SaleHouseParam param) throws SQLException {

        String sql = "select SALE_ID,SALE_SUBJECT,BUILD_NAME,CITY_ID,UPDATE_TIME,SALE_STATUS" +
                "        ,SALE_TOTAL_PRICE,SALE_INNERAREA,SALE_DIRECT,SALE_ROOM,SALE_WEI " +
                "        from FUN_SALE " +
                "        where SALE_STATUS != 100" +
                "        and city_id = ?" +
                "        and sale_total_price <= ?" +
                "        and sale_total_price >= ?" +
                "        and sale_room = ?";
        Connection conn = ConnHolder.getConnection();
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setInt(1, param.getCity_id());
        prep.setDouble(2, param.getMax_price());
        prep.setDouble(3, param.getMin_price());
        prep.setInt(4, param.getSale_room());
        prep.execute();
        ArrayList<SaleHouse> result = handlerResult(prep.getResultSet());
        JdbcUtil.close(prep, prep.getResultSet());
        return result;
    }

    @SuppressWarnings("all")
    public List<SaleHouse> selectByPage(SaleHouseParam param) throws SQLException {

        String sql = "select top " + param.getPage_size() +
                "  SALE_ID,SALE_SUBJECT,BUILD_NAME,CITY_ID,UPDATE_TIME,SALE_STATUS,SALE_TOTAL_PRICE,SALE_INNERAREA," +
                "  SALE_DIRECT,SALE_ROOM,SALE_HALL,SALE_WEI " +
                " from " +
                "        (select row_number() over(order by SALE_ID asc) as rownumber," +
                "        SALE_ID,SALE_SUBJECT,BUILD_NAME,CITY_ID,UPDATE_TIME,SALE_STATUS" +
                "        ,SALE_TOTAL_PRICE,SALE_INNERAREA," +
                "        SALE_DIRECT,SALE_ROOM,SALE_HALL,SALE_WEI " +
                "        from FUN_SALE " +
                "        where SALE_STATUS!=100" +
                "        and city_id=?" +
                "        and sale_total_price<=?" +
                "        and sale_total_price>=?" +
                "        and sale_room=?" +
                "        ) temp " +
                " where rownumber>?";

        Connection conn = ConnHolder.getConnection();
        PreparedStatement prep = conn.prepareStatement(sql);
        populateParam(param, prep);
        prep.execute();
        ArrayList<SaleHouse> result = handlerResult(prep.getResultSet());
        JdbcUtil.close(prep, prep.getResultSet());
        return result;
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
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.execute();
        ResultSet resultSet = prep.getResultSet();
        int num = 0;
        if (resultSet.next()) {
            num = resultSet.getInt(1);
        }
        JdbcUtil.close(prep, resultSet);
        return num;
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
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setInt(1, param.getCity_id());
        prep.setDouble(2, param.getMax_price());
        prep.setDouble(3, param.getMin_price());
        prep.setInt(4, param.getSale_room());
        prep.execute();
        ResultSet resultSet = prep.getResultSet();
        int num = -1;
        if (resultSet.next()) {
            num = resultSet.getInt(1);
        }
        JdbcUtil.close(prep, resultSet);
        return num;
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
     * @param prep
     * @throws SQLException
     */
    private void setParamValue(SaleHousePO house, PreparedStatement prep) throws SQLException {
        //。。
        prep.setObject(1, house.getSale_id());
        prep.setObject(2, house.getComp_id());
        prep.setObject(3, house.getCity_id());
        prep.setObject(4, house.getDept_id());
        prep.setObject(5, new java.sql.Date(house.getCreation_time().getTime()));
        prep.setObject(6, house.getSale_no());
        prep.setObject(7, house.getSale_useage());
        prep.setObject(8, house.getSale_area());
        prep.setObject(9, house.getSale_total_price());
        prep.setObject(10, house.getSale_source());
        prep.setObject(11, house.getSale_explrth());
        prep.setObject(12, house.getSale_consign());
        prep.setObject(13, house.getSale_map());
        prep.setObject(14, house.getSale_level());
        prep.setObject(15, house.getPlate_type());
        prep.setObject(16, house.getSale_status());
        prep.setObject(17, house.getShare_flag());
        prep.setObject(18, house.getRed_flag());
        prep.setObject(19, house.getFrom_public());
        prep.setObject(20, house.getSale_id_old());
        prep.setObject(21, house.getHouse_bargain());
        prep.setObject(22, house.getPanorama_map());
        prep.setObject(23, house.getYouyou_deal());
        prep.setObject(24, house.getIs_sale_lease());
        prep.setObject(25, house.getHouse_situation());
        prep.setObject(26, house.getOld_true_flag());
        prep.setObject(27, house.getSale_innerarea());
        prep.setObject(28, house.getSale_direct());
        prep.setObject(29, house.getSale_room());
        prep.setObject(30, house.getSale_hall());
        prep.setObject(31, house.getSale_wei());
        prep.setObject(32, house.getSale_subject());
        prep.setObject(33, house.getBuild_name());
        prep.setObject(34, new java.sql.Date(house.getUpdate_time().getTime()));
    }

    /**
     * 处理结果
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private ArrayList<SaleHouse> handlerResult(ResultSet resultSet) throws SQLException {

        ArrayList<SaleHouse> objects = new ArrayList<>();
        while (resultSet.next()) {
            //每行数据
            SaleHouse saleHouse = handlerSingleBean(resultSet);
            objects.add(saleHouse);
        }
        return objects;
    }

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

    /**
     * 查询赋值
     *
     * @param param
     * @param prep
     * @throws SQLException
     */
    private void populateParam(SaleHouseParam param, PreparedStatement prep) throws SQLException {
        prep.setObject(1, param.getCity_id());
        prep.setObject(2, param.getMax_price());
        prep.setObject(3, param.getMin_price());
        prep.setObject(4, param.getSale_room());
        prep.setObject(5, param.getOffset());
    }

}
