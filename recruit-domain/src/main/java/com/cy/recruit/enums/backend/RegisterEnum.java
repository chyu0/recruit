package com.cy.recruit.enums.backend;

import lombok.Getter;

public enum  RegisterEnum {

    REGISTER_SUCCESS(200, "用户注册成功"),
    EMAIL_NOTNULL(10000,"邮箱不能为空"),
    PASSWORD_NOTNULL(10001,"密码不能为空"),
    VERIFY_CODE_NOTNULL(10003,"验证码不能为空"),
    RESET_PASSWORD_NOT_MATCHING(10004,"重置密码与密码不匹配"),
    EMAIL_SO_LONG(10005,"邮箱超长"),
    PASSWORD_SO_LONG(10006,"密码超长"),
    VERIFY_CODE_ERROR(10007,"验证码错误"),
    OPERATE_FREQUENTLY(10008, "操作太频繁，请稍后再试"),
    PARAMS_ERROR(10009, "参数错误"),
    EMAIL_HAD_REGISTERED(10010,"邮箱已经注册，换个邮箱试试"),
    INNER_ERROR(500,"服务器内部错误");

    @Getter
    private int code;
    @Getter
    private String message;

    RegisterEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public RegisterEnum getRegisterEnumByCode(int code){
        for(RegisterEnum registerEnum : RegisterEnum.values()){
            if(code == registerEnum.getCode()){
                return registerEnum;
            }
        }
        return null;
    }

    public boolean equals(RegisterEnum registerEnum){
        return registerEnum != null && this.code == registerEnum.getCode();
    }
}
