package com.cy.recruit.service.jdbc.backend;

import com.cy.recruit.ServiceResult;
import com.cy.recruit.model.backend.BackendUser;

public interface BackendUserService {

    BackendUser getUserInfo(String email);

    ServiceResult registerUser(BackendUser backendUser);
}
