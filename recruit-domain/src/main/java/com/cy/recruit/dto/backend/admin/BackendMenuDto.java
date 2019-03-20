package com.cy.recruit.dto.backend.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class BackendMenuDto implements Serializable{

    private int parentId;

    private int hasChild;
}
