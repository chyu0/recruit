package com.cy.recruit.controller;

import com.alibaba.fastjson.JSONObject;
import com.cy.recruit.ServiceResult;
import com.cy.recruit.base.annotaion.NeedRefer;
import com.cy.recruit.base.backend.LoginAuthToken;
import com.cy.recruit.base.backend.RegisterAuthToken;
import com.cy.recruit.base.modal.ResultInfo;
import com.cy.recruit.common.utils.CommonUtils;
import com.cy.recruit.common.utils.CookieUtils;
import com.cy.recruit.common.utils.EntryDesUtils;
import com.cy.recruit.enums.backend.LoginEnum;
import com.cy.recruit.enums.backend.RegisterEnum;
import com.cy.recruit.model.backend.BackendUser;
import com.cy.recruit.redis.api.RedisClient;
import com.cy.recruit.service.jdbc.backend.BackendUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@NeedRefer
@Slf4j
@RestController
@RequestMapping("")
public class LoginController extends BaseController {

    @Resource
    private RedisClient redisClient;

    @Resource
    private BackendUserService backendUserService;

    @Value("${max_login_fail_frequently}")
    private int MAX_LOGIN_FREQUENT;

    @Value("${max_login_fail_show_authcode}")
    private int MAX_LOGIN_SHOW_AUTHCODE;

    @Value("${login_entry_key}")
    private String LOGIN_ENTRY_KEY;

    @Value("${default_expire_time}")
    private long DEFAULT_EXPIRE_TIME = 3600 * 1000;

    @Value("${default_login_cookie_expire_time}")
    private int DEFAULT_COOKIE_TIME = 3600;

    @Value("${default_cookie_domain}")
    private String DEFAULT_COOKIE_DOMAIN = "";

    @Value("${login_times_key}")
    private String LOGIN_TIMES_KEY;

    @Value("${verify_code_key}")
    private String VERIFY_CODE;

    private String SEPARATOR = "_";

    @Value("${email_max_length}")
    private int emailMaxLength = 50;

    @Value("${password_max_length}")
    private int passwordMaxLength = 50;

    @Value("${register_times_key}")
    private String REGISTER_TIMES_KEY;

    @Value("${register_fail_times_key}")
    private String REGISTER_FAIL_TIMES_KEY;

    @Value("${max_register_fail_frequently}")
    private long MAX_REGISTER_FAIL_TIMES = 20;

    @Value("${max_register_times}")
    private long MAX_REGISTER_TIMES = 2;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResultInfo login(HttpServletRequest request, HttpServletResponse response, String userName, String password, String authCode) {
        LoginEnum loginStatus = null;
        LoginAuthToken.Builder authBuilder = new LoginAuthToken.Builder();
        try{
            if (StringUtils.isBlank(userName)) {//邮箱空
                loginStatus = LoginEnum.EMAIL_NOTNULL;
                authBuilder.loginStatus(loginStatus).build();
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            }
            if (StringUtils.isBlank(password)) {//密码空
                loginStatus = LoginEnum.PASSWORD_NOTNULL;
                authBuilder.loginStatus(LoginEnum.PASSWORD_NOTNULL).build();
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            }
            String ip = CommonUtils.getIp(request);
            if (StringUtils.isBlank(ip)) {
                loginStatus = LoginEnum.PASSWORD_ERROR;//未查询到ip地址直接提示地址错误
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            }

            String key = LOGIN_TIMES_KEY + ip;
            String loginTimes = redisClient.get(key);
            if (StringUtils.isNotBlank(loginTimes) && Integer.parseInt(loginTimes) >= MAX_LOGIN_FREQUENT) {//超过最大可登录次数，限制登录
                loginStatus = LoginEnum.LOGIN_FREQUENTLY;
                authBuilder.loginStatus(loginStatus).build();
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            }

            long now = System.currentTimeMillis();
            boolean verifyCodeFlag = false;
            if (StringUtils.isNotBlank(loginTimes) && Integer.parseInt(loginTimes) > MAX_LOGIN_SHOW_AUTHCODE) {//是否显示验证码
                String source = CommonUtils.getContextPathByRefer(request);
                String redisCode = redisClient.get(source + VERIFY_CODE + ip, String.class);
                if (StringUtils.isNotBlank(redisCode)) {
                    String[] codeAndTime = redisCode.split(SEPARATOR);
                    if (codeAndTime.length == 2) {
                        long time = Long.parseLong(codeAndTime[1]);
                        if (now > time + 60 * 1000L || !codeAndTime[0].equalsIgnoreCase(authCode)) {
                            loginStatus = LoginEnum.VERIFY_CODE_ERROR;
                        } else {
                            verifyCodeFlag = true;
                        }
                    } else {
                        loginStatus = LoginEnum.INNER_ERROR;
                    }
                } else {
                    loginStatus = LoginEnum.VERIFY_CODE_NOTNULL;
                }
            } else {
                verifyCodeFlag = true;
            }

            if (!verifyCodeFlag) {//验证码错误记录错误次数统计
                authBuilder.showAuthCode(true);
                redisClient.increment(key);//自增，初始化0，并加1
                redisClient.setExpire(key, DEFAULT_EXPIRE_TIME);//重新设置过期时间
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            }

            // shiro认证
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                subject.login(token);
            } catch (UnknownAccountException e) {
                loginStatus = LoginEnum.PASSWORD_ERROR;
                authBuilder.loginStatus(loginStatus);
                if (StringUtils.isNotBlank(loginTimes) && Integer.parseInt(loginTimes) >= MAX_LOGIN_SHOW_AUTHCODE) {//显示验证码
                    authBuilder.showAuthCode(true);
                }
                redisClient.increment(key);//自增，初始化0，并加1
                redisClient.setExpire(key, DEFAULT_EXPIRE_TIME);//重新设置过期时间
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            } catch (DisabledAccountException e) {
                loginStatus = LoginEnum.ACCOUNT_DISABLE;
                authBuilder.loginStatus(loginStatus);
                return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
            }
            loginStatus = LoginEnum.LOGIN_SUCCESS;
            redisClient.setExpire(key, 0);//重新设置过期时间
            //可通过这种方式获取已登录的用户信息
            BackendUser user = (BackendUser) subject.getPrincipal();
            String entryToken = EntryDesUtils.encrypt(user.getToken(), LOGIN_ENTRY_KEY);
            //写入cookie
            CookieUtils.setCookie(response, "token", entryToken, DEFAULT_COOKIE_DOMAIN, "/", DEFAULT_COOKIE_TIME);
            authBuilder.token(entryToken);
            authBuilder.email(userName);
            authBuilder.loginStatus(loginStatus);
            return success().setData(authBuilder.build()).setMessage(loginStatus.getMessage());
        }catch (Exception e){
            log.error("登录失败，服务器内部错误", e);
            loginStatus = LoginEnum.INNER_ERROR;
            authBuilder.loginStatus(loginStatus);
            return fail().setCode(loginStatus.getCode()).setMessage(loginStatus.getMessage()).setData(authBuilder.build());
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResultInfo register(HttpServletRequest request, HttpServletResponse response, String userName, String password, String resetPassword, String authCode) {
        RegisterEnum registerStatus = null;
        RegisterAuthToken.Builder authBuilder = new RegisterAuthToken.Builder();

        try{
            String ip = CommonUtils.getIp(request);
            String failKey = REGISTER_FAIL_TIMES_KEY + ip;
            String successKey = REGISTER_TIMES_KEY + ip;
            String failTimes = redisClient.get(failKey);
            String successTimes = redisClient.get(successKey);
            //注册失败次数>最大限制（20） 或者 注册成功次数 > 最大限制（2）,防刷
            if((StringUtils.isNotBlank(failTimes) && Long.parseLong(failTimes) >= MAX_REGISTER_FAIL_TIMES)||(StringUtils.isNotBlank(successTimes) && Long.parseLong(successTimes) >= MAX_REGISTER_TIMES)){
                registerStatus = RegisterEnum.OPERATE_FREQUENTLY;
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }

            if(StringUtils.isBlank(userName)){
                registerStatus = RegisterEnum.EMAIL_NOTNULL;
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }
            if(StringUtils.isBlank(password)){
                registerStatus = RegisterEnum.PASSWORD_NOTNULL;
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }
            if(userName.length() > emailMaxLength){
                registerStatus = RegisterEnum.EMAIL_SO_LONG;
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }
            if(password.length() > passwordMaxLength){
                registerStatus = RegisterEnum.PASSWORD_SO_LONG;
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }
            if(!password.equals(resetPassword)){
                registerStatus = RegisterEnum.RESET_PASSWORD_NOT_MATCHING;
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }
            long now = System.currentTimeMillis();
            boolean verifyCodeFlag = false;
            String source = CommonUtils.getContextPathByRefer(request);
            String redisCode = redisClient.get(source + VERIFY_CODE + ip, String.class);
            if (StringUtils.isNotBlank(redisCode)) {
                String[] codeAndTime = redisCode.split(SEPARATOR);
                if (codeAndTime.length == 2) {
                    long time = Long.parseLong(codeAndTime[1]);
                    if (now > time + 60 * 1000L || !codeAndTime[0].equalsIgnoreCase(authCode)) {
                        registerStatus = RegisterEnum.VERIFY_CODE_ERROR;
                    } else {
                        verifyCodeFlag = true;
                    }
                } else {
                    registerStatus = RegisterEnum.INNER_ERROR;
                }
            } else {
                registerStatus = RegisterEnum.VERIFY_CODE_NOTNULL;
            }
            if (!verifyCodeFlag) {//验证码错误，记录注册失败次数
                redisClient.increment(failKey);
                redisClient.setExpire(failKey, DEFAULT_EXPIRE_TIME);
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }

            BackendUser backendUser = new BackendUser();
            backendUser.setEmail(userName);
            backendUser.setPassword(password);
            ServiceResult<RegisterEnum> serviceResult = backendUserService.registerUser(backendUser);
            if(!RegisterEnum.REGISTER_SUCCESS.equals(serviceResult.getData())){//注册失败
                registerStatus = serviceResult.getData();
                authBuilder.loginStatus(registerStatus).build();
                return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
            }
            //注册成功计数，防刷
            redisClient.increment(successKey);
            redisClient.setExpire(successKey, DEFAULT_EXPIRE_TIME);

            // shiro认证
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                subject.login(token);
            }catch (Exception e){
                log.error("注册成功后自动登录失败！！！", e);
                //这里返回成功，前端直接跳转到登录页，用户自行登录，不走自动登录逻辑
                return success().setData(authBuilder.build()).setMessage(RegisterEnum.REGISTER_SUCCESS.getMessage());
            }
            //可通过这种方式获取已登录的用户信息
            BackendUser user = (BackendUser) subject.getPrincipal();
            String entryToken = EntryDesUtils.encrypt(user.getToken(), LOGIN_ENTRY_KEY);
            //写入cookie
            CookieUtils.setCookie(response, "token", entryToken, DEFAULT_COOKIE_DOMAIN, "/", DEFAULT_COOKIE_TIME);
            authBuilder.token(entryToken);
            authBuilder.email(userName);
            return success().setData(authBuilder.build()).setMessage(RegisterEnum.REGISTER_SUCCESS.getMessage());
        }catch (Exception e){
            log.error("注册失败，服务器内部错误！");
            registerStatus = RegisterEnum.INNER_ERROR;
            authBuilder.loginStatus(registerStatus).build();
            return fail().setCode(registerStatus.getCode()).setMessage(registerStatus.getMessage()).setData(authBuilder.build());
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResultInfo logout(HttpServletRequest request, HttpServletResponse response){
        //清楚cookie信息token
        CookieUtils.removeCookie(request, response, "token", DEFAULT_COOKIE_DOMAIN, "/");
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.error("退出登录失败，请重试！");
            return fail().setMessage("退出登录失败，请稍后重试");
        }
        return success();
    }
}
