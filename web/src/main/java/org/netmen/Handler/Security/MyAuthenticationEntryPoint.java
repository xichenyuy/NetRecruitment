package org.netmen.Handler.Security;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.netmen.common.result.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 未认证入口点处理
 * 客户端进行认证提交时出现异常 或者是匿名用户访问受限资源的处理器
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String localizedMessage = authException.getLocalizedMessage();

        Result<Object> result = Result.error();

        if(authException instanceof BadCredentialsException){
            result.code(HttpServletResponse.SC_UNAUTHORIZED).message(localizedMessage);
        } else if(authException instanceof InternalAuthenticationServiceException){
            result.code(HttpServletResponse.SC_UNAUTHORIZED).message("用户名为空");
        } else{
            result.code(600).message("匿名用户无权限访问");
        }

        //将结果转化为json字符串
        String json = JSON.toJSONString(result);

        //返回json给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
