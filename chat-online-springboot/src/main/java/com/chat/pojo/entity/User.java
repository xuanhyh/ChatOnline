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
// 有了上面三个注解，只需要写属性即可
// 各属性的set、get方法以及无参构造、全参构造会自动生成
public class User implements Serializable {
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
}
