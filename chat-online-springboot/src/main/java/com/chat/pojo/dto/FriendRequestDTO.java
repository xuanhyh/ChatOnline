package com.chat.pojo.dto;

import lombok.Data;

@Data
public class FriendRequestDTO {
    private Long toUserId;
    private String fromUserUserName;
    private String fromUserName;
    private String sendTime;
}
