package com.cy.recruit.mapper.backend.admin;

import com.cy.recruit.dto.backend.admin.BackendMenuDto;
import com.cy.recruit.model.backend.admin.BackendMenu;

import java.util.List;

public interface BackendMenuMapper {

    List<BackendMenu> getMenuList(BackendMenuDto dto);

    List<BackendMenu> getAllMenuList();

}
