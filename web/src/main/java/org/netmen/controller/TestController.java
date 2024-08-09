package org.netmen.controller;

import org.netmen.common.result.Result;
import org.netmen.dao.po.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public Result<Object> resultTest(){
        User user = new User();
        user.setUsername("yang");
        user.setPassword("123456");
        user.setDeleted(false);
        System.out.println(Result.error().data(user));
        return Result.error().data(user);
    }
}
