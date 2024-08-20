package org.netmen.Handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        Object principal = authentication.getPrincipal();   //用户身份信息
        Object credentials = authentication.getCredentials();   //用户凭证信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();   //获取用户权限信息

        HashMap result = new HashMap();
        result.put("code", 0);
        result.put("message", "登录成功");
        result.put("data", principal);

        //将结果转化为json字符串
        String json = JSON.toJSONString(result);

        //返回json给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
