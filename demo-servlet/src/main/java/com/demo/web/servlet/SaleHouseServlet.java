package com.demo.web.servlet;

import com.alibaba.fastjson.JSON;
import com.demo.exceptions.ParamErrorException;
import com.demo.pojo.SaleHouse;
import com.demo.pojo.SaleHousePO;
import com.demo.pojo.SaleHouseParam;
import com.demo.result.DataResult;
import com.demo.result.Result;
import com.demo.service.SaleService;
import com.demo.service.impl.SaleServiceImpl;
import com.demo.util.DateConverter;
import com.demo.util.ParamParseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class SaleHouseServlet extends BaseServlet {


    /**
     * 添加或修改
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("all")
    public Result addAndEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //获取参数
            String json = ParamParseUtil.readJSONData(req);
            Map<String, String> map = JSON.parseObject(json, Map.class);
            ConvertUtils.register(new DateConverter(), Date.class);
            SaleServiceImpl saleService = new SaleServiceImpl();
            //新增
            if (StringUtils.isBlank(map.get("sale_id"))) {
                if (addNew(map)) return Result.getResult(Result.SUCCESS, "success");
            } else {
//                修改
                SaleHouse saleHouse = new SaleHouse();
                BeanUtils.populate(saleHouse, map);
                saleHouse.setUpdate_time(new Date());
                boolean b = saleService.updateHouseDetailById(saleHouse);
                if (b) {
                    return Result.getResult(Result.SUCCESS, "success");
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Result.getResult(Result.FAIL, "exceptions");
    }

    /**
     * 添加
     *
     * @param map 参数map
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private boolean addNew(Map<String, String> map) throws IllegalAccessException, InvocationTargetException {
        SaleHousePO saleHouse = new SaleHousePO();
        BeanUtils.populate(saleHouse, map);
        Date date = new Date();
        saleHouse.setCreation_time(date);
        saleHouse.setUpdate_time(date);
        if (1 == new SaleServiceImpl().addSaleHouse(saleHouse)) {
            return true;
        }
        return false;
    }

    /**
     * id查询
     *
     * @param req
     * @param resp
     * @return
     */
    public Result getById(HttpServletRequest req, HttpServletResponse resp) {
        String sale_id = req.getParameter("sale_id");
        //check param
        checkNumber(sale_id);
        SaleHouse detail = new SaleServiceImpl().getHouseDetailById(Integer.valueOf(sale_id));
        return DataResult.getDataResult(detail);
    }

    /**
     * id参数检查
     *
     * @param sale_id id
     */
    private void checkNumber(String sale_id) {
        boolean parsable = NumberUtils.isParsable(sale_id);
        if (!parsable || Integer.valueOf(sale_id) < 0) {
            throw new ParamErrorException("the param id exceptions");
        }
    }

    /**
     * 根据id删除
     *
     * @param req
     * @param resp
     * @return
     */
    public Result delete(HttpServletRequest req, HttpServletResponse resp) {
        String sale_id = req.getParameter("sale_id");
        checkNumber(sale_id);
        if (new SaleServiceImpl().delHouseById(Integer.valueOf(sale_id))) {
            return Result.getResult(Result.SUCCESS, "success");
        }
        return Result.getResult(Result.FAIL, "fail");
    }

    /**
     * 分页查询
     *
     * @param req
     * @param resp
     * @return
     */
    public Result getPageResult(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = ParamParseUtil.readJSONData(req);
            Map map = JSON.parseObject(json, Map.class);
            SaleHouseParam param = new SaleHouseParam();
            BeanUtils.populate(param, map);
            SaleService saleService = new SaleServiceImpl();
            return saleService.getPageResult(param);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Result.getResult(Result.FAIL, "server exceptions");
    }
}
