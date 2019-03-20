package com.cy.recruit.mongo;

import com.cy.recruit.model.User;
import com.cy.recruit.mongo.template.MongoDbTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserMongoDao {

    @Resource
    private MongoDbTemplate userMongoDbTemplate;

    public List<User> queryByName(String name){
        Criteria criteria = Criteria.where("name").is(name);
        Query query = new Query();
        query.addCriteria(criteria);
        return userMongoDbTemplate.queryList(query, User.class);
    }
}
