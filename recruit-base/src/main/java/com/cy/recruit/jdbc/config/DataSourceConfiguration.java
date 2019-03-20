package com.cy.recruit.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.cy.recruit.jdbc.setting.DataSourceSettingProperties;
import com.cy.recruit.jdbc.setting.DruidSettingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Configuration
public class DataSourceConfiguration {

    @Resource(name = "druidSettingProperties")
    private DruidSettingProperties druid;

    @Resource(name = "dataSourceSettingProperties")
    private DataSourceSettingProperties dataSource;

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.addUrlMappings(druid.getUrlMapping());
        reg.setServlet(new StatViewServlet());
        reg.addInitParameter("loginUsername", druid.getUsername());
        reg.addInitParameter("loginPassword", druid.getPassword());
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //WebStatFilter用于采集web-jdbc关联监控的数据。
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", druid.getExclusions());
        filterRegistrationBean.addInitParameter("profileEnable", String.valueOf(druid.isProfileEnable()));
        filterRegistrationBean.addInitParameter("principalCookieName", druid.getCookieName());
        filterRegistrationBean.addInitParameter("principalSessionName", druid.getSessionName());
        return filterRegistrationBean;
    }

    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSource.getDbUrl());
        datasource.setUsername(dataSource.getUsername());
        datasource.setPassword(dataSource.getPassword());
        datasource.setDriverClassName(dataSource.getDriverClassName());
        datasource.setInitialSize(dataSource.getInitialSize());
        datasource.setMinIdle(dataSource.getMinIdle());
        datasource.setMaxActive(dataSource.getMaxActive());
        datasource.setMaxWait(dataSource.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSource.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSource.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSource.getValidationQuery());
        datasource.setTestWhileIdle(dataSource.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSource.isTestOnBorrow());
        datasource.setTestOnReturn(dataSource.isTestOnReturn());
        datasource.setPoolPreparedStatements(dataSource.isPoolPreparedStatements());
        try {
            datasource.setFilters(dataSource.getFilters());
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
        return datasource;
    }
}
