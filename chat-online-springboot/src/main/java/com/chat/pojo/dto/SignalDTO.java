package com.chat.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalDTO {
    private String type;

    private String msg;

    private int status;

    private Object data;
}
