package com.cy.recruit.controller;

import com.alibaba.fastjson.JSONObject;
import com.cy.recruit.base.modal.ResultInfo;
import com.cy.recruit.model.User;
import com.cy.recruit.redis.api.RedisClient;
import com.cy.recruit.service.jdbc.UserService;
import com.cy.recruit.service.mongo.UserMongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("")
public class IndexController extends BaseController{

    @Resource
    private RedisClient redisClient;

    @Resource
    private UserMongoService userMongoService;

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping("/index")
    public ResultInfo sayHello() {
        log.error("这里是日志");
        String value = "你好啊！！！";
        String key = "hello1";
        redisClient.set(key, value, 39000L);

        String v = redisClient.get(key, String.class);

        String vnull = redisClient.get("", String.class);

        log.debug("v=" + v);

        //List<User> list = userMongoService.getUserByName("aaaaaa");

        List<User> list = userService.getAll();

        log.info("list=" + JSONObject.toJSONString(list));

        return fail().setCode(4001);
    }
}
