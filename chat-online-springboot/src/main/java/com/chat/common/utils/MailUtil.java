package com.chat.common.utils;

import com.chat.pojo.dto.MailDTO;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class MailUtil{
    private String fromEmail = "3295320346@qq.com";
    private String fromEmailPw = "tkcswpodkhskdaii";
    private String myEmailSMTPHost = "smtp.qq.com";
    private Properties properties; // 用于参数配置
    private Session session; // 用户创建会话对象
    /*
     * 获取验证码
     * @return 验证码字符串
     */
    @Getter
    private String verifyCode = MailCodeUtil.GenerateVerifyCode();;
    /**
     * 构造函数
     *
     */
    public MailUtil() {
        properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");  // 使用的协议
        properties.setProperty("mail.smtp.host", myEmailSMTPHost);  // 发件人的邮箱的 SMTP 服务器地址
        properties.setProperty("mail.smtp.auth", "true");  // 打开认证
        session = Session.getInstance(properties);
    }

    /*
     * 构建邮件内容
     * @param 收件人
     * @return 发送邮件内容
     */
    public MimeMessage createMailContent(String toMail) throws
            Exception, MessagingException {
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(fromEmail, "在线聊天系统邮箱验证", "UTF-8"));
        // 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
        // 邮件主题
        message.setSubject("验证码", "UTF-8");
        // 邮件正文
        message.setContent("您好，您的注册验证码是: " + verifyCode + "。", "text/html;charset=UTF-8");
        // 设置发件时间
        message.setSentDate(new Date());
        // 保存设置
        message.saveChanges();;
        return message;
    }
    /*
     * 发送邮件
     * @param 收件人
     */
    public void sendEmail(String toEmail) throws Exception {
        Transport transport = session.getTransport();
        transport.connect(fromEmail, fromEmailPw);
        MimeMessage message = createMailContent(toEmail);  // 邮件内容
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }
}
