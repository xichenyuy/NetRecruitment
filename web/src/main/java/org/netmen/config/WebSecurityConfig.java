package org.netmen.config;

import org.netmen.dao.config.DBUserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
// @EnableWebSecurity  //开启springSecurity的自定义配置 如果是springboot项目可以省略该注解 autoconfig会整合预定义配置
public class WebSecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    /*@Bean
    public UserDetailsService userDetailsService() {
        //创建基于内存的用户信息管理器
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //创建UserDetail对象
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        return manager;
    }*/

    // 可以通过@Component自动注入
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     //创建基于数据库的用户信息管理器
    //     DBUserDetailsManager manager = new DBUserDetailsManager();
    //     manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
    //     return manager;
    // }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //请求放行 SecurityFilterChain为表示15个安全过滤器链的对象
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //接口调试时需要关闭csrf攻击防御
        http.csrf(csrf -> csrf.disable());
        //配置请求的拦截方式
        http.authorizeRequests(authorize -> authorize
                .requestMatchers("/user/register","/login")
                .permitAll()
                //对所有请求开启授权保护
                .anyRequest()
                //已认证的请求会被自动授权
                .authenticated()
        );
        //跨域 暂时使用默认跨域
        http.cors(withDefaults());


        http.formLogin(form->{
            form.loginPage("/login")
                    .successHandler(new MyAuthenticationSuccessHandler())   //认证成功
                    .failureHandler(new MyAuthenticationFailureHandler())   //认证失败
            ;
        });  //使用表单授权
        //.httpBasic(withDefaults()); //使用浏览器弹出的默认授权方式 通常是无特殊样式的弹出框

        //注销
        http.logout(logout ->{
            logout.logoutSuccessHandler(new MyLogoutSuccessHandler());
        });

        http.exceptionHandling(exception ->{
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint());   //请求未认证
            exception.accessDeniedHandler(new MyAccessDeniedHandler());
        });

        http.sessionManagement(session ->{
            session.maximumSessions(1).expiredSessionStrategy(new MySessionInformationExpiredStrategy());
        });
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
