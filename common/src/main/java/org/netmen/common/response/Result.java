package org.netmen.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor  //无参构造方法
@AllArgsConstructor //全参构造方法
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> Result <E> success(E data){
        return new Result<>(0, "操作成功", data);
    }

    public static Result success(){
        return new Result(0, "操作成功", null);
    }

    public static Result error(String message){
        return new Result(-1, message, null);
    }
}
