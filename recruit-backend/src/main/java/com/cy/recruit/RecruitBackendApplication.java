package com.cy.recruit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication()
public class RecruitBackendApplication{

	public static void main(String[] args) {
		SpringApplication.run(RecruitBackendApplication.class, args);
	}

	/**
	 * 设置路由匹配规则，之匹配.do的请求
	 * @param dispatcherServlet
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean<DispatcherServlet> servletServletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet);
		servletServletRegistrationBean.addUrlMappings("*.do");
		return servletServletRegistrationBean;
	}
}

