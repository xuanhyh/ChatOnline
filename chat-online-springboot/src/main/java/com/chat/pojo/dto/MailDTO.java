package com.chat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailDTO implements Serializable {
    private String email; // 收件人邮箱
    private String emailKey; // 邮箱激活码 // 无用
}
