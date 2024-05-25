package com.chat.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 后端发送给前端的message
public class ResultMessage {
    private boolean system;
    private boolean fromGroup;

    private Long toId;//新增
    private Long toGroupId;//新增

    private Long fromId;
    private Object message;

    private String name;
    private String time;
}
