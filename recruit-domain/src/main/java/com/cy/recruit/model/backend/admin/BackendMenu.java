package com.cy.recruit.model.backend.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BackendMenu implements Serializable{

    private Integer id;

    private String menuName;

    private String linkUrl;

    private String vue;

    private Integer parentId;

    private boolean child;

    private List<BackendMenu> childMenus;
}
