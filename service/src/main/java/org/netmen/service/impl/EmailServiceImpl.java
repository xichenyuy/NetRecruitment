package org.netmen.service.impl;

import org.netmen.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import otg.netmen.common.dto.EmailProperties;
import otg.netmen.common.utils.MailUtil;

public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailProperties emailProperties;

    /**
     * @param to 接收人邮箱
     * @param title 邮件标题
     * @param content 邮件正文
     * @return
     */
    @Override
    public boolean send(String to, String title, String content) {
        System.out.println(emailProperties);
        boolean flag = MailUtil.sendMail(emailProperties, to, title, content);
        return false;
    }
}
