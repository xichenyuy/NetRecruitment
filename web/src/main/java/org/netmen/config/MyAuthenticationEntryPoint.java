package org.netmen.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;

/**
 * 未认证入口点处理
 * 客户端进行认证提交时出现异常 或者是匿名用户访问受限资源的处理器
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String localizedMessage = authException.getLocalizedMessage();

        HashMap result = new HashMap();
        result.put("code", -1);
        if(authException instanceof BadCredentialsException){
            result.put("message", localizedMessage);
        } else if(authException instanceof InternalAuthenticationServiceException){
            result.put("message", "用户名为空");
        } else{
            result.put("message", "匿名用户无权限访问");
        }

        //将结果转化为json字符串
        String json = JSON.toJSONString(result);

        //返回json给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
