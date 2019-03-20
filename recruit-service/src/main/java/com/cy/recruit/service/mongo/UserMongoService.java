package com.cy.recruit.service.mongo;

import com.cy.recruit.model.User;

import java.util.List;

public interface UserMongoService {

    List<User> getUserByName(String name);
}
