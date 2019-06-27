package com.demo.service.impl;

import com.demo.dao.SaleDao;
import com.demo.dao.impl.SaleDaoImpl;
import com.demo.exceptions.PersistentConnectionException;
import com.demo.exceptions.QuerySqlException;
import com.demo.jdbc.ConnHolder;
import com.demo.pojo.SaleHouse;
import com.demo.pojo.SaleHousePO;
import com.demo.pojo.SaleHouseParam;
import com.demo.result.PageResult;
import com.demo.service.SaleService;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Slf4j
public class SaleServiceImpl implements SaleService {

    private static AtomicInteger idGenerator = new AtomicInteger();
    private static volatile boolean initIdGenerator;


    void initFlag() {
        if (!initIdGenerator) {
            synchronized (SaleServiceImpl.class) {
                if (!initIdGenerator) {
                    initializeId();
                }
            }
        }
    }

    /**
     * 添加
     *
     * @param house
     * @return
     */
    public int addSaleHouse(SaleHousePO house) {
        ConnHolder.checkConnection();
        try {
            initFlag();
            SaleDao saleDao = new SaleDaoImpl();
            house.setSale_id(idGenerator.incrementAndGet());
            house.setSale_area(house.getSale_innerarea());
            return saleDao.insertRecord(house);
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        } finally {
            ConnHolder.releaseQuietly();
        }
    }


    /**
     * id查询
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public SaleHouse getHouseDetailById(Integer id) {
        ConnHolder.checkConnection();
        SaleHouse saleHouse = null;
        try {
            saleHouse = new SaleDaoImpl().selectById(id);
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        } finally {
            ConnHolder.releaseQuietly();
        }
        return saleHouse;
    }

    /**
     * 修改
     *
     * @param house
     * @return
     */
    public boolean updateHouseDetailById(SaleHouse house) {
        ConnHolder.checkConnection();
        int num = 0;
        try {
            num = new SaleDaoImpl().updateById(house);
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        } finally {
            ConnHolder.releaseQuietly();
        }
        return 1 == num;
    }

    public boolean delHouseById(Integer id) {
        ConnHolder.checkConnection();
        int num = 0;
        try {
            num = new SaleDaoImpl().deleteById(id);
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        } finally {
            ConnHolder.releaseQuietly();
        }
        return 1 == num;
    }


    public List<SaleHouse> getAllList(SaleHouseParam param) {
        ConnHolder.checkConnection();
        try {
            return new SaleDaoImpl().selectAllList(param);
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        } finally {
            ConnHolder.releaseQuietly();
        }
    }

    //    public PageResult
    @SuppressWarnings("unchecked")
    public PageResult<SaleHouse> getPageResult(SaleHouseParam param) {
        //查询总记录
        ConnHolder.checkConnection();
        try {
            SaleDao saleDao = new SaleDaoImpl();
            if (param.getSale_room() <= 0) {
                param.setSale_room(2);
            }
            //
            if (param.getMax_price() < param.getMin_price()) {
                Double max_price = param.getMax_price();
                param.setMax_price(param.getMin_price());
                param.setMin_price(max_price);
            }
            int total = new SaleDaoImpl().selectTotalRecord(param);
            List<SaleHouse> list = saleDao.selectByPage(param);
            return PageResult.<SaleHouse>getPageResult(list, total, param.getPage_size());
        } catch (SQLException e) {
            log.error("数据库sql查询异常.", e);
            throw new QuerySqlException(e.getMessage());
        } finally {
            ConnHolder.releaseQuietly();
        }
    }


    private void initializeId() {
        try {
            int maxId = new SaleDaoImpl().getMaxId();
            idGenerator.set(maxId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
