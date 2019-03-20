package com.cy.recruit.service.jdbc.backend;

import com.cy.recruit.model.backend.admin.BackendMenu;

import java.util.List;
import java.util.Map;

public interface BackendMenuService {

    Map<Integer, BackendMenu> getMenuList();
}
