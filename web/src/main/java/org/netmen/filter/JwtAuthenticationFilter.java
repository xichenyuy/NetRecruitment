package org.netmen.filter;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.netmen.common.utils.JwtUtil;
import org.netmen.exception.MyAuthenticationException;
import org.netmen.config.MyAuthenticationFailureHandler;
import org.netmen.dao.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String uri = request.getRequestURI();    //localhost:8080/hello
            if(!uri.equals("/user/login")) { //登录接口直接放行
               this.validateToken(request);
            }
        } catch (AuthenticationException e) {
            new MyAuthenticationFailureHandler().onAuthenticationFailure(request, response, e);
        }
        //放行
        filterChain.doFilter(request, response);
    }

    //用于token校验
    private void validateToken(HttpServletRequest request){
        //判断令牌是否为空
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)) {
            //token可能在url中
            token = request.getParameter("Authorization");
        }
        if(StringUtils.isEmpty(token)) {
            throw new MyAuthenticationException("token为空");
        }
        //redis 校验 token
        String redisToken = stringRedisTemplate.opsForValue().get(token);
        if(StringUtils.isEmpty(redisToken)) {
            throw new MyAuthenticationException("token已过期");
        }
        //校验令牌
        LoginUser loginUser = null;
        try {
            Claims claims = JwtUtil.parseJwt(token);
            String jsonString = claims.getSubject();  //取出的是json字符串
            loginUser = JSON.parseObject(jsonString, LoginUser.class);  //把json字符串转成LoginUser对象
        } catch (Exception e) {
            throw new MyAuthenticationException("token校验失败");
        }
        //把验证完获取到的用户信息再次放入spring security的上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
