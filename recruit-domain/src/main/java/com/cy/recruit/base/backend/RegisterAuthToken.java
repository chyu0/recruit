package com.cy.recruit.base.backend;

import com.cy.recruit.enums.backend.RegisterEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterAuthToken implements Serializable{

    private String auth_token;//用户加密信息

    private String token;//用户标识

    private RegisterEnum registerStatus; //状态码

    private String email;//用户邮箱

    public RegisterAuthToken(Builder builder){
        this.auth_token = builder.auth_token;
        this.token = builder.token;
        this.registerStatus = builder.registerStatus;
        this.email = builder.email;
    }

    //builder设计模式
    public static class Builder {
        private String auth_token;//用户加密信息

        private String token;//用户标识

        private RegisterEnum registerStatus; //状态码

        private String email;//用户邮箱

        public Builder authToken(String auth_token){
            this.auth_token = auth_token;
            return this;
        }

        public Builder token(String token){
            this.token = token;
            return this;
        }

        public Builder loginStatus(RegisterEnum registerStatus){
            this.registerStatus = registerStatus;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public RegisterAuthToken build() {
            return new RegisterAuthToken(this);
        }
    }
}
