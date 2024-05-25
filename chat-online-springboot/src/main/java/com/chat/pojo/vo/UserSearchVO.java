package com.chat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchVO implements Serializable {
    private Long userId;
    private String name; // 昵称
    private String username; // 用户名 唯一

    private String sex;
    private String avatarUrl;
    private Integer state;


}
