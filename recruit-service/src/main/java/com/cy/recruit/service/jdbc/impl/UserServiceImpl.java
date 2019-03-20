package com.cy.recruit.service.jdbc.impl;

import com.cy.recruit.mapper.UserMapper;
import com.cy.recruit.model.User;
import com.cy.recruit.service.jdbc.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }
}
