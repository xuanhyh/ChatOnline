package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grouping {
    private Long userId;
    private Long groupingId;
    private String groupingName;
}
