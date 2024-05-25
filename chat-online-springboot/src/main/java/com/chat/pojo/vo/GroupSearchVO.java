package com.chat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSearchVO implements Serializable {
    private Long groupId;
    private String groupName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
