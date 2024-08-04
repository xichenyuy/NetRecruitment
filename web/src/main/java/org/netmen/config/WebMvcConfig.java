package org.netmen.config;

import org.netmen.common.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor)   //添加拦截器
                .excludePathPatterns("/user/login", "/user/register")  //放行接口
                .excludePathPatterns("/swagger**/**",
                    "/webjars/**",
                    "/v3/**",
                    "/doc.html")
                .excludePathPatterns("/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}
