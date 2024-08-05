package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.netmen.dao.config.DBUserDetailsManager;
import org.netmen.dao.mapper.UserMapper;
import org.netmen.dao.po.User;
import org.netmen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.netmen.common.utils.Md5Util;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Resource
    private DBUserDetailsManager dbUserDetailsManager;

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
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .build();
        dbUserDetailsManager.createUser(userDetails);
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
