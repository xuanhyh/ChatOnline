package com.chat.config;

import com.chat.common.properties.SMMSProperties;
import com.chat.common.utils.SMMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建并返回一个SMMSUtil对象，需要用到的地方直接注入即可
 */
@Configuration
@Slf4j
public class SMMSConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SMMSUtil smmsUtil(SMMSProperties smmsProperties){
        log.info("开始创建SMMS文件上传工具类，{}",smmsProperties);
        return new SMMSUtil(smmsProperties.getToken());
    }
}
