package com.demo.boot.controller;

import com.demo.boot.pojo.SaleHouse;
import com.demo.boot.pojo.SaleHouseParam;
import com.demo.boot.service.SaleService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ExportController {

    @Autowired
    private SaleService saleService;

    @GetMapping("/export")
    @SuppressWarnings("all")
    public void export(HttpServletResponse resp, SaleHouseParam param) {

        try {
            String fileName = "data.xml";
            // 返回文件相应头设置
//            resp.setContentType("");
            resp.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            resp.setHeader("Content-Type", "multipart/form-data;charset=utf-8");

            List<SaleHouse> list = saleService.getExportList(param);
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
            System.out.println("生成rss.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成rss.xml失败");
        }

    }

}
