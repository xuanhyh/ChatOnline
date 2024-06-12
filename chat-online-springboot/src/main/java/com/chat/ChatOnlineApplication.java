package com.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
//@EntityScan("com.chat.pojo.entity") // 指定实体类所在的包路径
@EnableJpaRepositories(basePackages = "com.chat.repository") // 指定JPARepository扫描位置
public class ChatOnlineApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatOnlineApplication.class, args);
        log.info("服务已启动");
    }

}
