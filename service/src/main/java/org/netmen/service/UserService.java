package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.User;

import java.util.Map;

public interface UserService extends IService<User> {
    User findByUsername(String username);

    Map<String, Object> login(User user);

    void register(String username, String password);

    void updateInfo(Integer userId, String nickname, String email);

    void updatePic(Integer userId, String avatarUrl);

    void updatePwd(Integer userId, String newPwd, String token);


}
