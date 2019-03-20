package com.cy.recruit.vo.backend.admin;

import com.cy.recruit.model.backend.admin.BackendMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BackendMenuVO extends BackendMenu implements Serializable {

    private List<BackendMenuVO> childBackendMenu;
}
