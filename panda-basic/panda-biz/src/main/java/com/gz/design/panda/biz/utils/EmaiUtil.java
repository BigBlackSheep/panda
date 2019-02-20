package com.gz.design.panda.biz.utils;

import java.util.List;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public NewResponseUtil sendEmail(String fromAddress, String[] toAddress, String title, String content) {

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
        } catch (Exception e) {
            log.error("发送简单邮件失败", e);
            return NewResponseUtil.newFailureResponse("发送失败");
        }
        return NewResponseUtil.newSucceedResponse("发送成功");

    }

    public NewResponseUtil sendEmailHtml(String fromAddress, List<String> toAddresList, String title, String content) {
        try {
            //建立邮件消息
            MimeMessage message = mailSender.createMimeMessage();
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //发送者
            helper.setFrom(fromAddress);
            //设置多个收件人地址
            InternetAddress[] addressArray = new InternetAddress[toAddresList.size()];
            for (int i = 0; i < toAddresList.size(); i++) {
                addressArray[i] = new InternetAddress(toAddresList.get(i));
            }
            helper.setTo(addressArray);
            //发送的标题
            helper.setSubject(title);
            //发送的内容
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("发送Html邮件失败", e);
            return NewResponseUtil.newFailureResponse("发送失败");
        }
        return NewResponseUtil.newSucceedResponse("发送成功");
    }

}
