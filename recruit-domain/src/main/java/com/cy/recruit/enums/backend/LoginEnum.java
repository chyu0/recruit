package com.cy.recruit.enums.backend;

import lombok.Getter;

public enum LoginEnum {

    LOGIN_SUCCESS(200, "用户登录成功"),
    PASSWORD_ERROR(10001, "账号或密码错误"),
    EMAIL_NOTNULL(10002, "邮箱不能为空"),
    PASSWORD_NOTNULL(10003, "密码不能为空"),
    LOGIN_FREQUENTLY(10004, "登录过于频繁，请稍后重试"),
    VERIFY_CODE_ERROR(10005, "验证码错误"),
    ACCOUNT_DISABLE(10006,"账号不可用"),
    ACCOUNT_LOCKED(10007,"账号被锁定"),
    VERIFY_CODE_NOTNULL(10008, "验证码不能为空"),
    INNER_ERROR(500, "服务器内部错误");

    @Getter
    private int code;
    @Getter
    private String message;

    LoginEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static LoginEnum getLoginEnumByCode(int code){

        for(LoginEnum loginEnum : LoginEnum.values()){
            if(code == loginEnum.getCode()){
                return loginEnum;
            }
        }
        return null;
    }
}
