package com.chat.common.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chat.smms")//Spring 在启动时自动读取配置文件中以 chat.smms 为前缀的属性，属性见application.yml
@Data
public class SMMSProperties {
    private String token;
}
