package org.netmen.Handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        HashMap result = new HashMap();
        result.put("code", -1);
        result.put("message", "没有权限");

        //将结果转化为json字符串
        String json = JSON.toJSONString(result);

        //返回json给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println();
    }
}
