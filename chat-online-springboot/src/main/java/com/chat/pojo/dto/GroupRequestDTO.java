package com.chat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupRequestDTO implements Serializable {
    private Long groupRequestId;
    private String status;
}
