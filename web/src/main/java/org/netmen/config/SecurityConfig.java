package org.netmen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 所有请求都需要认证，认证方式：httpBasic
        http.authorizeHttpRequests(auth ->{
            auth.anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults());    //Authonrization: 用户名和密码的Base64编码 Basic Base64-encoded(username:password)

        return http.build();
    }
}
