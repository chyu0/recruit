package com.cy.recruit;

import lombok.Data;

import java.util.Map;

@Data
public class ServiceResult<T> {

    private boolean success;
    private String message;
    private T data;
    private Map<String ,Object> attr;
}
