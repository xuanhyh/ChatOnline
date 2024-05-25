package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember implements Serializable {
    private Long groupId;
    private Long memberId;
    private Integer memberPermission;
}
