package com.chat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSignUpDTO implements Serializable {
    private String name;
    private String username;
    private String password;
    private String email;
    private String verifyCode;
}
