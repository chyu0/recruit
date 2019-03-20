package com.cy.recruit.jdbc.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@Validated
@PropertySource("classpath:properties/jdbc/druid.properties")
@ConfigurationProperties(prefix = "druid")
@Setter
@Getter
public class DruidSettingProperties {
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String urlMapping = "/druid/*";
    private String exclusions = "";
    private boolean profileEnable;
    private String cookieName = "COOKIE_NAME";
    private String sessionName = "SESSION_NAME";
}
