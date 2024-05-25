package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Long id;
    private Integer chatFlag;
    private Integer contentFlag;
    private String textMessage;
    private String pictureMessage;
    private String videoMessage;
    private Long senderId;
    private String senderName;
    private LocalDateTime sendTime;
    private String sendTimeString;
    private Long receiverId;
    private Long receiveGroupId;
    private boolean received;
}
