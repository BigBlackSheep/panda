package com.gz.design.panda.biz.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @Author wangqiang
 * @Date 2018/8/14 15:28
 **/
@Component
@Slf4j
public class EmaiUtil {

    @Autowired
    JavaMailSender mailSender;

    public NewResponseUtil sendEmail(String fromAddress,String toAddress,String title,String content){

        try {
            //建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            //发送者
            mainMessage.setFrom(fromAddress);
            //接收者
            mainMessage.setTo(toAddress);
            //发送的标题
            mainMessage.setSubject(title);
            //发送的内容
            mainMessage.setText(content);
            mailSender.send(mainMessage);
        }catch (Exception e) {
            log.error("发送邮件失败",e);
            return NewResponseUtil.newFailureResponse("发送失败");
        }
        return NewResponseUtil.newSucceedResponse("发送成功");

    }

}
