package com.chat.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserGroupRequestVO implements Serializable {
    private Long groupId;
    private Long groupCreatorId;
    private String status;
}
