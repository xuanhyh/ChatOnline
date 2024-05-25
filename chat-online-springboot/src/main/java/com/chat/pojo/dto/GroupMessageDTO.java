package com.chat.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 前端发送给后端的群聊消息
public class GroupMessageDTO {
    private Long fromId;
    private Long toGroupId;
    private String message;
    private String name;
    private String time;
}
