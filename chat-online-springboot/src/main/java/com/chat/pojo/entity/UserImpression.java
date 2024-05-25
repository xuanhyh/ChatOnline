package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImpression implements Serializable {
    private Long impressionId;
    private Long userId;
    private Long senderId;
    private Integer contentFlag;
    private String impressionText;
    private String impressionPicture;
}
