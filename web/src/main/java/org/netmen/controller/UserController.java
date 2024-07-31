package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.netmen.common.utils.JwtUtil;
import org.netmen.dao.po.User;
import org.netmen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.netmen.common.response.Result;
import org.netmen.common.utils.Md5Util;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "传入账号密码注册账号")
    public Result register(@Pattern(regexp = "^\\${5,16}$") String username, @Pattern(regexp = "^\\${5,16}$") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "传入账号密码登录账号")
    public Result<String> login(@Pattern(regexp = "^\\${5,16}$") String username, @Pattern(regexp = "^\\${5,16}$") String password) {
        User loginUser = userService.findByUsername(username);
        //判断用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }
        //判断密码是否正确
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        } else {
            return Result.error("密码错误");
        }
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> claims = new HashMap<>();
        String username = (String) claims.get("username");
        User user = userService.findByUsername(username);
        return Result.success(user);
    }
}
