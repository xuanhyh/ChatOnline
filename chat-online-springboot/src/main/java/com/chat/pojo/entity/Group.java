package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {
    private Long groupId;
    private String groupName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
