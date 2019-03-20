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
@PropertySource("classpath:properties/jdbc/mybatis.properties")
@ConfigurationProperties(prefix = "mybatis")
@Setter
@Getter
public class MybatisSettingProperties {

    @NotNull
    private String scanPackage;
    @NotNull
    private String configPath;
    @NotNull
    private String typeAlias;
    @NotNull
    private String mapperPath;
}
