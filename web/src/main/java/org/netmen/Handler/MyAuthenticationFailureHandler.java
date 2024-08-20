package org.netmen.Handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.netmen.common.result.Result;
import org.netmen.common.exception.MyAuthenticationException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        Result<Object> result = Result.error().code(500);

        if(exception instanceof AccountExpiredException){
            result.message( "用户过期，登录失败" );
        } else if (exception instanceof BadCredentialsException){
            result.message( "用户名或密码错误，登录失败" );
        } else if (exception instanceof CredentialsExpiredException){
            result.message( "密码过期，登录失败" );
        } else if (exception instanceof DisabledException){
            result.message( "用户被禁用，登录失败" );
        } else if (exception instanceof LockedException){
            result.message( "用户被锁，登录失败" );
        } else if (exception instanceof InternalAuthenticationServiceException){
            result.message( "用户不存在，登录失败" );
        } else if (exception instanceof MyAuthenticationException){
            result.code(600).message(exception.getLocalizedMessage());
        } else {
            result.message("登录失败");
        }

        //将结果转化为json字符串
        String json = JSON.toJSONString(result);

        //返回json给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
