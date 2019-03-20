package com.cy.recruit.interceptor;

import com.cy.recruit.base.annotaion.NeedRefer;
import com.cy.recruit.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 登录拦截器
 */
@Slf4j
@Component
public class CorsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        NeedRefer needRefer = method.getAnnotation(NeedRefer.class);
        Class clazz = method.getDeclaringClass();

        NeedRefer clazzNeedRefer = (NeedRefer) clazz.getAnnotation(NeedRefer.class);
        //有needRefer注解表示必须有来源信息
        if((needRefer != null && needRefer.isNeed()) || (clazzNeedRefer != null && clazzNeedRefer.isNeed())){
            String refer = request.getHeader("Referer");//有refer，且refer有正确来源
            if(StringUtils.isBlank(refer) || !CommonUtils.isUrl(refer)){
                return false;
            }
        }
        return true;
    }
}
