package com.cy.recruit.base.modal;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ResultInfo<T> implements Serializable{

    private int code;
    private String message;
    private T data;
    private Map<String, Object> attrs;

    public static ResultInfo newInstance(){
        return new ResultInfo();
    }

    public ResultInfo setCode(int code){
        this.code = code;
        return this;
    }

    public ResultInfo setMessage(String message){
        this.message = message;
        return this;
    }

    public ResultInfo setData(T data){
        this.data = data;
        return this;
    }

    public ResultInfo put(String key, Object value){
        if(attrs == null){
            this.attrs = new HashMap<>();
        }
        this.attrs.put(key, value);
        return this;
    }
}
