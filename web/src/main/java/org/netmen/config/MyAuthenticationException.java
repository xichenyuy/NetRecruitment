package org.netmen.config;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义认证异常类
 */
public class MyAuthenticationException extends AuthenticationException {
    public MyAuthenticationException(String msg) {
        super(msg);
    }
}
