package org.netmen.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor //全参构造方法
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    //私有构造函数 防止外部直接实例化
    private Result(){}

    //成功结果的静态工厂方法
    public static <E> Result <E> success(){
        return new Result<>(ResultCode.SUCCESS, "操作成功", null);
    }

    //错误结果的静态工厂方法
    public static <E> Result <E> error(){
        return new Result<>(ResultCode.ERROR, "操作失败", null);
    }

    //链式编程
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
    public Result<T> message(String message){
        this.setMessage(message);
        return this;
    }
    public Result<T> data(T data){
        this.setData(data);
        return this;
    }

}
