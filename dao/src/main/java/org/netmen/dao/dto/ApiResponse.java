package org.netmen.dao.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private int code;
    private String message;
    // 其他可能需要的字段，比如数据data等

    // 构造器、getter和setter方法...

    // 创建一个静态方法来构建成功的响应
    public ApiResponse success(String message) {
        ApiResponse response = new ApiResponse();
        response.setCode(0); // 设置状态码为0表示成功
        response.setMessage(message);
        return response;
    }
}
