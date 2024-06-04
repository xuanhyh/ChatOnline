package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest implements Serializable {
    private Long id;
    private Long fromUserId;
    private Long groupId;
    private Long groupCreatorId;
    private String status;
}
