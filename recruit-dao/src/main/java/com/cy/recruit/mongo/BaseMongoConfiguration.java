package com.cy.recruit.mongo;

import com.cy.recruit.mongo.template.MongoDbTemplate;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mongo/mongo-name.properties")
@ConfigurationProperties(prefix = "mongo.collections.name")
@Setter
public class BaseMongoConfiguration {

    private String user;//user集合

    @Bean(name="userMongoDbTemplate")
    public MongoDbTemplate userMongoDbTemplate(){
        MongoDbTemplate mongoDbTemplate = new MongoDbTemplate();
        mongoDbTemplate.setCollectionName(user);
        return mongoDbTemplate;
    }
}
