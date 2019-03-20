package com.cy.recruit.jdbc.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Component
@Validated
@PropertySource("classpath:properties/jdbc/mysql-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "spring.datasource")
@Setter
@Getter
public class DataSourceSettingProperties {

    @NotBlank
    private String dbUrl;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String driverClassName;
    private int initialSize = 1;
    private int minIdle = 0;
    private int maxActive = 10;
    private int maxWait = 2000;
    private int timeBetweenEvictionRunsMillis = 10000;
    private int minEvictableIdleTimeMillis = 10000;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private String filters;

}
