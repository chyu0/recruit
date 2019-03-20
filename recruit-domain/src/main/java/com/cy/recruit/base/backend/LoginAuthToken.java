package com.cy.recruit.base.backend;

import com.cy.recruit.enums.backend.LoginEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginAuthToken implements Serializable{

    private String auth_token;//用户加密信息

    private String token;//用户标识

    private LoginEnum loginStatus; //状态码

    private boolean showAuthCode;//是否显示验证码

    private String email;//用户邮箱

    public LoginAuthToken(Builder builder){
        this.auth_token = builder.auth_token;
        this.token = builder.token;
        this.loginStatus = builder.loginStatus;
        this.email = builder.email;
        this.showAuthCode = builder.showAuthCode;
    }

    //builder设计模式
    public static class Builder {
        private String auth_token;//用户加密信息

        private String token;//用户标识

        private LoginEnum loginStatus; //状态码

        private boolean showAuthCode;//是否显示验证码

        private String email;//用户邮箱

        public Builder authToken(String auth_token){
            this.auth_token = auth_token;
            return this;
        }

        public Builder token(String token){
            this.token = token;
            return this;
        }

        public Builder loginStatus(LoginEnum loginEnum){
            this.loginStatus = loginEnum;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder showAuthCode(boolean showAuthCode){
            this.showAuthCode = showAuthCode;
            return this;
        }

        public LoginAuthToken build() {

            initDefaultValue(this);

            return new LoginAuthToken(this);
        }

        public void initDefaultValue(Builder builder){
            if(builder.loginStatus == null){
                builder.loginStatus = LoginEnum.PASSWORD_ERROR;//默认账号密码错误
            }
        }
    }
}
