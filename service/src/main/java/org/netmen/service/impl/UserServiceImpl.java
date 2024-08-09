package org.netmen.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.netmen.common.utils.JwtUtil;
import org.netmen.dao.mapper.UserMapper;
import org.netmen.dao.po.User;
import org.netmen.dao.vo.LoginUser;
import org.netmen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.netmen.common.utils.Md5Util;

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
        map.put("token", jwt);
        map.put("username", loginUser.getUsername());
        System.out.println(map.toString());
        return map;
    }

    @Override
    public void updateInfo(User user) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, user.getId()).set(User::getNickname, user.getNickname()).set(User::getEmail, user.getEmail());
        userMapper.updateById(user);
    }

    @Override
    public void updatePic(Integer userId, String avatarUrl) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, userId).set(User::getUserPic, avatarUrl);
        userMapper.update(wrapper);
    }

    @Override
    public void updatePwd(User user, String newPwd) {
        user.setUpdateTime(null);//注意不要覆盖更新时间 使用updateById()时设置为null不修改
        user.setPassword(Md5Util.getMD5String(newPwd));
        userMapper.updateById(user);
    }
}
