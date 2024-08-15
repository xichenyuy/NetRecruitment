package org.netmen.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.netmen.common.exception.MyAuthenticationException;
import org.netmen.common.utils.JwtUtil;
import org.netmen.dao.mapper.UserMapper;
import org.netmen.dao.po.User;
import org.netmen.dao.vo.LoginUser;
import org.netmen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.netmen.common.utils.Md5Util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //设置username为唯一索引 可以根据username查到唯一用户
    @Override
    public User findByUsername(String username) {
        /*QueryWrapper：由于是字符串形式的条件拼接，编译时无法检查其中的错误和类型不匹配问题，所以需要开发人员自行保证查询条件的正确性。
        LambdaQueryWrapper：基于 Lambda 表达式构建查询条件，可以在编译阶段进行类型检查，编译器可以帮助检测属性名等错误，减少运行时出错的可能性。*/
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void register(String username, String password) {
//        UserDetails userDetails = org.springframework.security.core.userdetails.User
//                .withDefaultPasswordEncoder()
//                .username(username)
//                .password(password)
//                .build();
//        dbUserDetailsManager.createUser(userDetails);
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); //密码加密
        user.setNickname(username); //默认昵称为用户名
        user.setDisabled(true); //默认新建的账号不可用 需要管理员审核通过后才能开启账号
        userMapper.insert(user);
    }

    @Override
    public void updateInfo(Integer userId, String nickname, String email) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, userId).set(User::getNickname, nickname).set(User::getEmail, email);
        userMapper.update(wrapper);
    }

    @Override
    public Map<String, Object> login(User user) {
        //封装authentication对象
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //校验
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //放入用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //生成jwt 使用fastjson将对象转换为json字符串
        String jsonString = JSON.toJSONString(loginUser);
        //调用jwt工具生成令牌
        String jwt = JwtUtil.createJwt(jsonString, null);
        //存储redis白名单 key=jwt value=jwt
        stringRedisTemplate.opsForValue().set(jwt, jwt, JwtUtil.TTL/1000, TimeUnit.SECONDS);
        Map<String, Object> map = new HashMap<>();
        map.put("username", loginUser.getUsername());
        map.put("token", jwt);
        return map;
    }



    @Override
    public void updatePic(Integer userId, String avatarUrl) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, userId).set(User::getUserPic, avatarUrl);
        userMapper.update(wrapper);
    }

    @Override
    public void updatePwd(Integer userId, String newPwd, String token) {
        //校验令牌正确性
        LoginUser loginUser = null;
        try {
            Claims claims = JwtUtil.parseJwt(token);
            String jsonString = claims.getSubject();  //取出的是json字符串
            loginUser = JSON.parseObject(jsonString, LoginUser.class);  //把json字符串转成LoginUser对象
        } catch (Exception e) {
            throw new MyAuthenticationException("token校验失败");
        }
        if(!Objects.equals(loginUser.getUser().getId(), userId)){
            throw new MyAuthenticationException("token与用户信息不一致");
        }
        //更改密码
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, userId).set(User::getPassword, passwordEncoder.encode(newPwd));
        userMapper.update(wrapper);
        //删除redis中对应的token
        stringRedisTemplate.opsForValue().getOperations().delete(token);
    }
}
