package com.chat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpVO implements Serializable {

    private Long id;

    private String userName;

    private String name;

    private String token;

    private String email;


}
