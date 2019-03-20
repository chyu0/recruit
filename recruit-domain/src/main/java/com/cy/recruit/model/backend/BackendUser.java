package com.cy.recruit.model.backend;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BackendUser implements Serializable{

    private long id;
    private String name;
    private String email;
    private String password;
    private String token;
    private Integer enable;
    private Date registerTime;
}
