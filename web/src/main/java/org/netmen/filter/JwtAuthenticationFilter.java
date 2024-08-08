package org.netmen.filter;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.netmen.common.utils.JwtUtil;
import org.netmen.dao.vo.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();    //localhost:8080/hello
        if(uri.equals("/user/login")) { //登录接口直接放行
            filterChain.doFilter(request, response);
            return ;
        }
        if(uri.equals("/user/register")) {
            filterChain.doFilter(request, response);
            return ;
        }
        if(uri.equals("/user/logout")) {
            filterChain.doFilter(request, response);
            return ;
        }
        //判断令牌是否为空
        String token = request.getHeader("Authorization");
        if(!StringUtils.hasText(token)){
            throw new ServletException("Authorization header is required");
        }
        //校验令牌
        LoginUser loginUser = null;
        try {
            Claims claims = JwtUtil.parseJwt(token);
            String jsonString = claims.getSubject();  //取出的是json字符串
            loginUser = JSON.parseObject(jsonString, LoginUser.class);  //把json字符串转成LoginUser对象
        } catch (Exception e) {
            throw new RuntimeException("token校验失败");
        }
        //把验证完获取到的用户信息再次放入spring security的上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //放行
        filterChain.doFilter(request, response);
    }
}
