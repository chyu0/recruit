package com.cy.recruit.base.config;

import com.cy.recruit.base.shiro.realm.BackendUserAuthorRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Slf4j
@Configuration
public class ShiroConfiguration {

    @Resource
    private BackendUserAuthorRealm backendUserAuthorRealm;

    @Value("${login_url}")
    private String loginUrl;

    @Value("${home_url}")
    private String homeUrl;

    /**
     * 注入SecurityManager
     * @return SecurityManager
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        Set<Realm> realms = new HashSet<>();
        realms.add(backendUserAuthorRealm);
        manager.setRealms(realms);
        return manager;
    }

    /**
     * 注入Filter
     * @param securityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl(loginUrl);
        filterFactoryBean.setSuccessUrl(homeUrl);
        // 配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/swagger-*/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html/**", "anon");
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");// 表示admin权限才可以访问，多个加引号用逗号相隔
        filterChainDefinitionMap.put("/login.do", "anon");//登录不拦截
        filterChainDefinitionMap.put("/authcode/verification.do", "anon");//验证码不拦截
        filterChainDefinitionMap.put("/register.do", "anon");//注册不拦截
        filterChainDefinitionMap.put("/logout.do", "anon");
        filterChainDefinitionMap.put("/*", "authc");// 表示需要认证才可以访问
        filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/*.*", "authc");
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return filterFactoryBean;
    }
}