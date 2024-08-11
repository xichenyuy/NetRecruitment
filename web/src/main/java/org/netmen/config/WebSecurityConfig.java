package org.netmen.config;

import org.netmen.Handler.*;
import org.netmen.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
// @EnableWebSecurity  //开启springSecurity的自定义配置 如果是springboot项目可以省略该注解 autoconfig会整合预定义配置
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启接口授权访问配置
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
                .requestMatchers("/user/register","/user/login")
                .permitAll()
                //对所有请求开启授权保护
                .anyRequest()
                //已认证的请求会被自动授权
                .authenticated()
        );
        //配置过滤器的执行顺序
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //跨域 暂时使用默认跨域
        http.cors(withDefaults());

        //使用表单授权
        http.formLogin(form->{
            // form.loginPage("/user/login");   //自定义登录接口 不走默认登录
            form.successHandler(new MyAuthenticationSuccessHandler());   //认证成功
            form.failureHandler(new MyAuthenticationFailureHandler());   //认证失败
        });
        //.httpBasic(withDefaults()); //使用浏览器弹出的默认授权方式 通常是无特殊样式的弹出框

        //注销
        http.logout(logout ->{
            logout.logoutSuccessHandler(new MyLogoutSuccessHandler());
        });

        http.exceptionHandling(exception ->{
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint());   //未认证用户无权限处理
            exception.accessDeniedHandler(new MyAccessDeniedHandler()); //认证用户无权限
        });

        http.sessionManagement(session ->{
            session.maximumSessions(1).expiredSessionStrategy(new MySessionInformationExpiredStrategy());   //多个浏览器只创建1个会话
        });
        return http.build();
    }
}
