package com.chat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String password;
}
