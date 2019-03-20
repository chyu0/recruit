package com.cy.recruit.mapper.backend.admin;

import com.cy.recruit.model.backend.BackendUser;

public interface BackendUserMapper {

    BackendUser getUserInfo(String userName);

    int registerUser(BackendUser backendUser);
}
