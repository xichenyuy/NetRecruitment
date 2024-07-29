package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.User;

public interface UserService extends IService<User> {
    User findByUsername(String username);

    void register(String username, String password);
}
