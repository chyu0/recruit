package com.cy.recruit.service.mongo.impl;

import com.cy.recruit.model.User;
import com.cy.recruit.mongo.UserMongoDao;
import com.cy.recruit.service.mongo.UserMongoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userMongoService")
public class UserMongoServiceImpl implements UserMongoService{

    @Resource
    private UserMongoDao userMongoDao;

    @Override
    public List<User> getUserByName(String name) {
        return userMongoDao.queryByName(name);
    }
}
