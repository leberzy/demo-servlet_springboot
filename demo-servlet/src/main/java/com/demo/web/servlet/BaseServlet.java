package com.demo.web.servlet;

import com.alibaba.fastjson.JSON;
import com.demo.exceptions.ParamErrorException;
import com.demo.exceptions.PersistentConnectionException;
import com.demo.result.Result;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class BaseServlet extends HttpServlet {

    @Override
    @SuppressWarnings("all")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
// 接收参数:
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String methodName = req.getParameter("method");
        if (methodName == null || "".equals(methodName)) {
            resp.getWriter().println("method参数为null!!!");
            return;
        }
        // 获得子类的Class对象：
        Class clazz = this.getClass();
        // 获得子类中的方法了:
        try {
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            // 使方法执行:
            Result result = (Result) method.invoke(this, req, resp);
            if (Objects.nonNull(result)) {
                String json = JSON.toJSONString(result);
                resp.getWriter().write(json);
                resp.flushBuffer();
            }
        } catch (ParamErrorException e) {
            log.error("参数异常...\n", e);
            String json = JSON.toJSONString(Result.getResult(Result.FAIL, e.getMessage()));
            resp.getWriter().write(json);
            resp.flushBuffer();
        } catch (PersistentConnectionException e) {
            log.error("DB连接异常...\n", e);
            String json = JSON.toJSONString(Result.getResult(Result.FAIL, e.getMessage()));
            resp.getWriter().write(json);
            resp.flushBuffer();
        } catch (Exception e) {
            log.error("服务器内部异常...\n", e);
            String json = JSON.toJSONString(Result.getResult(Result.FAIL, "server inner exceptions."));
            resp.getWriter().write(json);
            resp.flushBuffer();
        }

    }
}
