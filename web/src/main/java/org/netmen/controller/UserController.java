package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.netmen.common.utils.ThreadLocalUtil;
import org.netmen.dao.po.User;
import org.netmen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.netmen.common.result.Result;
import org.netmen.common.utils.Md5Util;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "传入账号密码注册账号")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error().message("用户名已被占用");
        }
    }

//    @PostMapping("/login")
//    @Operation(summary = "用户登录", description = "传入账号密码登录账号")
//    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
//        User loginUser = userService.findByUsername(username);
//        //判断用户是否存在
//        if (loginUser == null) {
//            return Result.error("用户名错误");
//        }
//        //判断密码是否正确
//        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", loginUser.getId());
//            claims.put("username", loginUser.getUsername());
//            String token = JwtUtil.genToken(claims);
//            //将token存储到redis中
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            operations.set(token, token, 1, TimeUnit.HOURS);
//            return Result.success(token);
//        }
//        return Result.error("密码错误");
//    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        Map<String, Object> map = userService.login(user);
        if(!map.get("token").equals("") && !map.get("username").equals("")) {
            return Result.success().data(map);
        }
        return Result.error().message("登录失败");
    }

    @PostMapping("logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //获取用户相关信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            //清空用户信息
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            //清空redis里的token
            stringRedisTemplate.delete(token);

        }
        return Result.success();
    }

//    @GetMapping("/userInfo")
//    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
//       // Map<String, Object> claims = JwtUtil.parseToken(token);
//       // String username = (String) claims.get("username");
//        Map<String, Object> claims = ThreadLocalUtil.get();
//        String username = (String) claims.get("username");
//        User user = userService.findByUsername(username);
//        return Result.success().data(user);
//    }
//
//    @PutMapping("/update")
//    public Result update(@RequestBody @Validated User user) {
//        Map<String, Object> claims = ThreadLocalUtil.get();
//        Integer userId = (Integer) claims.get("id");
//        if(!userId.equals(user.getId())){
//            return Result.error("用户id与token不匹配");
//        }
//        userService.updateInfo(user);
//        return Result.success();
//    }
//
//    @PatchMapping("/updateAvatar")
//    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
//        Map<String, Object> claims = ThreadLocalUtil.get();
//        Integer userId = (Integer) claims.get("id");
//        userService.updatePic(userId, avatarUrl);
//        return Result.success();
//    }
//
//    @PatchMapping("/updatePwd")
//    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
//        //参数校验
//        String oldPwd = params.get("old_pwd");
//        String newPwd = params.get("new_pwd");
//        String rePwd = params.get("re_pwd");
//        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
//            return Result.error("缺少必要参数");
//        }
//        //根据用户名获得源密码
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String username = map.get("username").toString();
//        User loginUser = userService.findByUsername(username);
//        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
//            return Result.error("源密码填写不正确");
//        }
//        if(!rePwd.equals(newPwd)){
//            return Result.error("两次填写的新密码不一致");
//        }
//        userService.updatePwd(loginUser, newPwd);
//        //删除redis中对应的token
//        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        operations.getOperations().delete(token);
//        return Result.success();
//    }
}
