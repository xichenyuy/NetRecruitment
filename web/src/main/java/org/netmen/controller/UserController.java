package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Permission;
import org.netmen.dao.po.User;
import org.netmen.dao.vo.LoginUser;
import org.netmen.dto.UserDTO;
import org.netmen.service.UserService;
import org.springframework.beans.BeanUtils;
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
    @Operation(summary = "注册", description = "传入账号密码注册账号 账号密码为5~16长度的字符数字 注册的账号默认不可用 需要管理审核后启用")
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
    @Operation(summary = "登录", description = "输入账号密码登录")
    public Result login(@RequestBody User user) {
        Map<String, Object> map = userService.login(user);
        if(!map.get("token").equals("") && !map.get("username").equals("")) {
            return Result.success().message("登录成功").data(map);
        }
        return Result.error().message("登录失败");
    }

    @PostMapping("logout")
    @Operation(summary = "登出", description = "账号登出")
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
    @Operation(summary = "用户管理列表", description = "分页查询所有用户信息 用于管理员管理用户信息")
    public Result list(Integer pageNum, Integer pageSize){
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userService.page(page);
        return Result.success().data(userPage);
    }

    @PostMapping
    @Operation(summary = "用户管理新增", description = "用于管理员直接新增用户账号 昵称将会用作默认账号名 生成默认密码123456")
    public Result add(@RequestBody @Validated UserDTO userDTO){
        User byUsername = userService.findByUsername(userDTO.getNickname());    //默认输入的nickname为用户名
        if (byUsername == null) {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setUsername(userDTO.getNickname());    //默认输入的nickname为用户名
            user.setPassword(passwordEncoder.encode("123456"));
            userService.save(user);
            return Result.success();
        } else {
            return Result.error().message("用户名已被占用");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "用户管理更新", description = "用于管理员更新用户信息和状态")
    public Result update(@PathVariable Integer id, @RequestBody @Validated User user){
        User byId = userService.getById(id);
        if(byId == null){
            return Result.error().message("ID不存在");
        }
        //重写update方法 防止不允许修改的字段如id username password 和自动填充字段
        userService.updateByAdmin(id, user);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "用户管理删除", description = "用于管理员删除账号")
    public Result delete(@PathVariable Integer id){
        User byId = userService.getById(id);
        if(byId == null){
            return Result.error().message("ID不存在");
        }
        userService.removeById(id);
        return Result.success();
    }


    //以下是用户个人操作

    @GetMapping("/detail")
    @Operation(summary = "用户个人详情", description = "用户更新个人的昵称 邮箱")
    public Result detail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = userService.getById(loginUser.getUser().getId());
        return Result.success().data(user);
    }

    @PutMapping("/updateInfo")
    @Operation(summary = "用户个人更新信息", description = "用户更新个人的昵称 邮箱")
    public Result updateInfo(@RequestBody @Validated UserDTO userDTO){ //用于更改 nickname email 和之后添加的个人信息 userPic要接入OSS服务器另写接口
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        userService.updateInfo(id, userDTO.getNickname(), userDTO.getEmail());
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    @Operation(summary = "用户个人更新头像", description = "用户更新个人头像")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        userService.updatePic(id, avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    @Operation(summary = "用户个人更新密码", description = "用户更新个人密码")
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
