package org.netmen.Handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.netmen.exception.MyAuthenticationException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        HashMap result = new HashMap();
        result.put("code", 500);
        String message = null;
        if(exception instanceof AccountExpiredException){
            message = "用户过期，登录失败";
        } else if (exception instanceof BadCredentialsException){
            message = "用户名或密码错误，登录失败";
        } else if (exception instanceof CredentialsExpiredException){
            message = "密码过期，登录失败";
        } else if (exception instanceof DisabledException){
            message = "用户被禁用，登录失败";
        } else if (exception instanceof LockedException){
            message = "用户被锁，登录失败";
        } else if (exception instanceof InternalAuthenticationServiceException){
            message = "用户不存在，登录失败";
        } else if (exception instanceof MyAuthenticationException){
            message = exception.getLocalizedMessage();
        } else {
            message = "登录失败";
        }
        result.put("message", message);

        //将结果转化为json字符串
        String json = JSON.toJSONString(result);

        //返回json给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
