package com.chat.pojo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="message")
public class Message implements Serializable {
    @Id
    private Long id; //主键加@Id注解
    private Integer chatFlag;
    private Integer contentFlag;
    private String textMessage;
    private String pictureMessage;
    private String videoMessage;
    private Long senderId;
    @Transient
    private String senderName;//这个不是表里有的，加@Transient告诉JPA忽略它
    private LocalDateTime sendTime;
    @Transient
    private String sendTimeString;//这个不是表里有的，加@Transient告诉JPA忽略它
    private Long receiverId;
    private Long receiveGroupId;
    private boolean received;
}
