package com.chat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateImpressionDTO implements Serializable {
    private Long impressionId;
    private Integer contentFlag;
    private String impressionText;
    private String impressionPicture;
}
