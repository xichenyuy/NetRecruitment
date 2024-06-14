package org.netmen.controller;

import org.netmen.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @RequestMapping("/send")
    public Boolean send(){
        String to = "17880790228@163.com";
        String title = "测试邮件";
        String content = "测试邮件的内容...";
        boolean flag = emailService.send(to, title, content);
        return flag;
    }
}
