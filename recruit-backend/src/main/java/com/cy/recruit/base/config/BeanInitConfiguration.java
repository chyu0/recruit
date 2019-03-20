package com.cy.recruit.base.config;

import com.cy.recruit.redis.api.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInitConfiguration {

    /**
     * 定义一个redis客户端，默认时间是expireTime
     * @param expireTime
     * @return
     */
    @Bean
    public RedisClient redisClient(@Value("${redis.default.expiretime}") Long expireTime){
        RedisClient redisClient = new RedisClient();
        redisClient.setDefault_expire_time(expireTime);
        return redisClient;
    }
}
