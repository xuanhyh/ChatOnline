package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String fromUserUserName;
    private String fromUserName;
    private String sendTime;
    private String status;
}
