package com.cy.recruit.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@Validated
@PropertySource("classpath:properties/redis/redis-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "redis")
@Getter
@Setter
public class RedisSettingProperties {

    @NotNull
    private String host;
    @NotNull
    private int port;
    private String pass;
    private int database;
    private long timeout;

    private Pool pool = new Pool();//redis连接池

    @Setter
    @Getter
    class Pool{
        private int maxTotal = 8;
        private int maxIdle = 8;
        private int minIdle = 0;
        private long maxWaitMillis = 2000;
        private long minEvictableIdleMillis=-1;
        private long softMinEvictableIdleMillis=-1;
        private int numTestsPerEvictionRun=3;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean testWhileIdle;
        private long timeBetweenEvictionRunsMillis = -1;
        private boolean blockWhenExhausted;
    }
}
