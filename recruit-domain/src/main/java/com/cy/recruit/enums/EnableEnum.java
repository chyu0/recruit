package com.cy.recruit.enums;

import lombok.Getter;

public enum EnableEnum {

    DISABLE(0, "无效"), ENABLE(1, "有效");

    @Getter
    private int code;
    @Getter
    private String message;

    EnableEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public boolean equals(Integer code){
        return code!=null && code.equals(this.code);
    }
}
