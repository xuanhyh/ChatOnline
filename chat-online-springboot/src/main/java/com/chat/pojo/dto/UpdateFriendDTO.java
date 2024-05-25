package com.chat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateFriendDTO implements Serializable {
    private Long friendId;
    private String remark;
}
