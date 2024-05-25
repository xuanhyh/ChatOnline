package com.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ChatOnlineApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatOnlineApplication.class, args);
        log.info("服务已启动");
    }

}
