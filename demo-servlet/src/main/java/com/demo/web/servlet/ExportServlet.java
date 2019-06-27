package com.demo.web.servlet;

import com.demo.pojo.SaleHouse;
import com.demo.pojo.SaleHouseParam;
import com.demo.service.impl.SaleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExportServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //参数处理
            Enumeration names = req.getParameterNames();
            Map<String, String> map = new HashMap<>();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                map.put(name, req.getParameter(name));
            }
            SaleHouseParam param = new SaleHouseParam();
            BeanUtils.populate(param, map);
            SaleServiceImpl saleService = new SaleServiceImpl();
            List<SaleHouse> houseList = saleService.getAllList(param);
            //导出
            export(houseList, resp);
        } catch (Exception e) {
            log.error("export exceptions", e);
        }


    }


    @SuppressWarnings("all")
    private void export(List<SaleHouse> list, HttpServletResponse resp) {

        try {
            String fileName = "data.xml";
            // 返回文件相应头设置
//            resp.setContentType("");
            resp.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            resp.setHeader("Content-Type", "multipart/form-data;charset=utf-8");
            // 创建document对象
            Document document = DocumentHelper.createDocument();
            // 创建根节点root
            Element root = document.addElement("root");
            // 生成子节点及子节点内容
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = 0; i < list.size(); i++) {
                Element saleHouse = root.addElement("saleHouse");

                Element saleId = saleHouse.addElement("saleId");
                saleId.addText(String.valueOf(list.get(i).getSale_id()));

                Element saleSubject = saleHouse.addElement("saleSubject");
                saleSubject.addText(String.valueOf(list.get(i).getSale_subject()));

                Element buildName = saleHouse.addElement("buildName");
                buildName.addText(String.valueOf(list.get(i).getBuild_name()));

                Element saleTotalPrice = saleHouse.addElement("saleTotalPrice");
                saleTotalPrice.addText(String.valueOf(list.get(i).getSale_total_price()));

                Element saleInnerarea = saleHouse.addElement("saleInnerarea");
                saleInnerarea.addText(String.valueOf(list.get(i).getSale_innerarea()));

                Element saleDirect = saleHouse.addElement("saleDirect");
                saleDirect.addText(String.valueOf(list.get(i).getSale_direct()));

                Element saleWei = saleHouse.addElement("saleWei");
                saleWei.addText(String.valueOf(list.get(i).getSale_wei()));

                Element updateTime = saleHouse.addElement("updateTime");
                LocalDateTime localDateTime = LocalDateTime.ofInstant(
                        list.get(i).getUpdate_time().toInstant(), ZoneId.systemDefault());
                String formatter = localDateTime.format(dateTimeFormatter);
                updateTime.addText(formatter);

                Element cityId = saleHouse.addElement("cityId");
                cityId.addText(String.valueOf(list.get(i).getSale_id()));

                Element saleStatus = saleHouse.addElement("saleStatus");
                saleStatus.addText(String.valueOf(list.get(i).getSale_status()));
            }
            // 设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 编码格式
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(resp.getWriter(), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.flush();
            writer.close();
            log.info("生成rss.xml成功");
        } catch (Exception e) {
            log.error("生成rss.xml失败", e);
        }

    }
}
