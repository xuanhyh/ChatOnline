package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserWithGroupingId implements Serializable {
    private Long userId;
    private String name; // 昵称
    private String username; // 用户名 唯一
    private String password;
    private String sex;
    private String avatarUrl;
    private Integer state;
    private String email;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long groupingId;//为了这个建的类
}