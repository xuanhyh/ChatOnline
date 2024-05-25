package com.chat.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chat.jwt")//Spring 在启动时自动读取配置文件中以 chat.jtw 为前缀的属性，属性见application.yml
@Data
public class JwtProperties {

    /**
     * 用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
