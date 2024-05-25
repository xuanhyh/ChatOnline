package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String time;
    private String status;
}
