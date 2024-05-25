package com.chat.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 前端发送给后端的message
public class MessageDTO {

    private Long fromId;
    private Long toId;
    private String message;
    private String name;
    private String time;
}
