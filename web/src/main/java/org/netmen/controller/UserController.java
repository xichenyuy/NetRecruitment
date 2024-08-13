package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.netmen.common.result.Result;
import org.netmen.dao.po.User;
import org.netmen.dao.vo.LoginUser;
import org.netmen.dto.UserDTO;
import org.netmen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "传入账号密码注册账号 账号密码为5~16长度的字符数字")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error().message("用户名已被占用");
        }
    }
    
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        Map<String, Object> map = userService.login(user);
        if(!map.get("token").equals("") && !map.get("username").equals("")) {
            return Result.success().message("登录成功").data(map);
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
        return Result.success().message("退出系统成功");
    }

    @GetMapping
    @Operation(summary = "用户列表", description = "分页查询所有用户信息 用于管理员管理用户信息")
    public Result list(Integer pageNum, Integer pageSize){
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userService.page(page);
        return Result.success().data(userPage);
    }

    //以下是用户个人操作
    @PutMapping("/updateInfo")
    @Operation(summary = "用户更新个人信息", description = "用户更新个人的昵称 邮箱")
    public Result updateInfo(@RequestBody @Validated UserDTO userDTO){ //用于更改 nickname email 和之后添加的个人信息 userPic要接入OSS服务器另写接口
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        userService.updateInfo(id, userDTO.getNickname(), userDTO.getEmail());
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    @Operation(summary = "用户更新个人头像", description = "用户更新个人头像")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        userService.updatePic(id, avatarUrl);
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


    @PatchMapping("/updatePwd")
    @Operation(summary = "用户更新个人密码", description = "用户更新个人密码")
    public Result updatePwd(@Pattern(regexp = "^\\S{5,16}$")String oldPwd,
                            @Pattern(regexp = "^\\S{5,16}$")String newPwd,
                            @Pattern(regexp = "^\\S{5,16}$")String rePwd,
                            @RequestHeader(name = "Authorization") String token) {
        //参数校验
        //也可以新建DTO使用 Valid注解 @Equals(field = "newPwd", message = "两次输入的密码不一致") 校验
        if(!rePwd.equals(newPwd)){
            return Result.error().message("两次填写的新密码不一致");
        }
        //根据用户名获得源密码
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = userService.findByUsername(loginUser.getUser().getUsername());
        boolean matches = passwordEncoder.matches(oldPwd, user.getPassword());
        if(!matches){
            return Result.error().message("源密码填写不正确");
        }
        userService.updatePwd(user.getId(), newPwd, token);
        return Result.success();
    }
}
