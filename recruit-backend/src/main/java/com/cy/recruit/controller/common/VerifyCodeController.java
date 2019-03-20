package com.cy.recruit.controller.common;


import com.cy.recruit.base.annotaion.NeedRefer;
import com.cy.recruit.common.utils.CommonUtils;
import com.cy.recruit.common.utils.SCaptchaUtils;
import com.cy.recruit.redis.api.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

@Controller
@RequestMapping("authcode")
@Slf4j
public class VerifyCodeController {

    @Resource
    private RedisClient redisClient;

    @Value("${verify_code_key}")
    private String VERIFY_CODE;

    private String SEPARATOR = "_";

    @NeedRefer
    @RequestMapping("verification")
    public void verification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long now = new Date().getTime();
        String time = request.getParameter("t");
        try{
            if(StringUtils.isBlank(time) || Long.parseLong(time) < now - 60 * 1000L || Long.parseLong(time) > now){
                log.error(String.format("验证码时间已过期！t=%s", time));
                return ;
            }
            String source = CommonUtils.getContextPathByRefer(request);
            // 设置响应的类型格式为图片格式
            response.setContentType("image/jpeg");
            // 禁止图像缓存。
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            //实例生成验证码对象
            SCaptchaUtils instance = new SCaptchaUtils();
            String code = instance.getCode();
            if(StringUtils.isNotBlank(source)){
                String ip = CommonUtils.getIp(request);
                //将验证码存入redis，仅有请求来源的时候才写入redis，否则不写入
                redisClient.set(source + VERIFY_CODE + ip, code + SEPARATOR + new Date().getTime(), 60L);
            }
            //向页面输出验证码图片
            instance.write(response.getOutputStream());
        }catch (Exception e){
            log.error(String.format("验证码时间已过期！t=%s",time), e);
        }
    }
}
