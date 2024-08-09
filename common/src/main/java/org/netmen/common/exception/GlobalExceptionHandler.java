package org.netmen.common.exception;

import org.netmen.common.result.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error().message(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "全局异常处理：操作失败");
    }

}
