package com.cy.recruit.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * 日志信息打印拦截器
 */
@Slf4j
@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> startTimeThreadLocal =
            new NamedThreadLocal<Long>("Controller StartTime");


    private String getParamString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String[]> e:map.entrySet()){
            sb.append(e.getKey()).append("=");
            String[] value = e.getValue();
            if(value != null && value.length == 1){
                sb.append(value[0]).append("\t");
            }else{
                sb.append(Arrays.toString(value)).append("\t");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(log.isDebugEnabled()){
            long startTime = System.currentTimeMillis();
            startTimeThreadLocal.set(startTime);//线程绑定变量（该数据只有当前请求的线程可见）
        }
        return true;
    }

    /**
     * 打印错误日志
     */
    public static String getStackTraceAsString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        if(e != null){
            e.printStackTrace(new PrintWriter(stringWriter));
        }
        return stringWriter.toString();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(log.isDebugEnabled()){
            long startTime = startTimeThreadLocal.get();
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            startTimeThreadLocal.remove();
            if(handler instanceof HandlerMethod){
                StringBuilder sb = new StringBuilder(1000);
                HandlerMethod h = (HandlerMethod) handler;
                sb.append("Controller: ").append(h.getBean().getClass().getName()).append("\n");
                sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
                sb.append("Params    : ").append(getParamString(request.getParameterMap())).append("\n");
                sb.append("URI       : ").append(request.getRequestURI()).append("\n");
                sb.append("CostTime  : ").append(executeTime).append("ms");
                log.debug(sb.toString());
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果controller报错，则记录异常错误
        if(log.isErrorEnabled() && ex != null){
            HandlerMethod h = (HandlerMethod) handler;
            String controllerName = h.getBean().getClass().getName();
            log.error(controllerName + "异常: " + getStackTraceAsString(ex));
        }
    }
}
