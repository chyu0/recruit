package com.cy.recruit.base.config;

import com.cy.recruit.interceptor.CorsInterceptor;
import com.cy.recruit.interceptor.LoggerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Slf4j
@Configuration
public class WebInitConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer matchConfigurer){
        matchConfigurer.setUseSuffixPatternMatch(false);//禁用url后缀匹配
        matchConfigurer.setUseTrailingSlashMatch(true);//url有结束符‘/’与没有等价
        //禁用路径后缀匹配，如果需要匹配*.do在RecruitBackendApplication中添加servletRegistrationBean进行路由匹配，该值置为true
        matchConfigurer.setUseRegisteredSuffixPatternMatch(true);
    }

    /**
     * 设置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //这里可以添加多个拦截器
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/**");
    }

    /**
     * 设置默认跳转，包括首页等，以及后面一些默认跳转
     * @param registry
     */
    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        registry.addViewController("/").setViewName("forward:/index.do");
    }

    @Value("${cors.allow.origin}")
    private String[] origin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origin)
                .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                .allowCredentials(true).maxAge(3600);
    }

}
