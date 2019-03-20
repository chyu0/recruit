package com.cy.recruit.redis.api;

import com.alibaba.fastjson.JSONObject;
import com.cy.recruit.redis.template.BytesRedisTemplate;
import com.cy.recruit.redis.utils.ProtoStuffSerializerUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

/**
 * RedisClient客户端
 */
@Slf4j
public class RedisClient {

    @Resource
    private BytesRedisTemplate bytesRedisTemplate;

    @Setter
    private Long default_expire_time = 24 * 3600L;

    /**
     * redis赋值，setEx会覆盖旧值
     * @param key
     * @param object
     * @param expireTime
     */
    public void set(String key , Object object, Long expireTime){
        try{
            if(StringUtils.isBlank(key) || object == null){
                log.warn(String.format("写入redis失败，检查到key或者value为空，key=%s,value=%s", key, JSONObject.toJSON(object)));
                return ;
            }
            if(expireTime == null){
                expireTime = default_expire_time;
            }
            bytesRedisTemplate.setEx(key, ProtoStuffSerializerUtils.serialize(object), expireTime);
        }catch (Exception e){
            log.error(String.format("写入redis失败, key=%s,object=%s,expireTime=%s", key, JSONObject.toJSONString(object), expireTime), e);
        }
    }

    /**
     * 从redis中获取缓存的值
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key , Class<T> clazz){
        try{
            if(StringUtils.isBlank(key) || clazz == null){
                log.warn(String.format("读取redis失败，检查到key或者class为空，key=%s，class=%s", key, clazz));
                return null;
            }
            byte[] result = bytesRedisTemplate.get(key);
            return ProtoStuffSerializerUtils.deserialize(result, clazz);
        }catch (Exception e){
            log.error(String.format("读取redis失败,key=%s,class=%s",key,clazz), e);
            return null;
        }
    }

    /**
     * 从redis中获取缓存的值，默认是string类型
     * @param key
     * @return
     */
    public String get(String key){
        try{
            if(StringUtils.isBlank(key)){
                log.warn(String.format("读取redis失败，检查到key为空，key=%s", key));
                return null;
            }
            byte[] result = bytesRedisTemplate.get(key);
            return new String(result);
        }catch (Exception e){
            log.error(String.format("读取redis失败,key=%s", key), e);
            return null;
        }
    }

    /**
     * 删除指定key的redis缓存
     * @param key
     */
    public void remove(String key){
        try{
            if(StringUtils.isBlank(key)){
                log.warn(String.format("删除redis失败，检查到key为空，key=%s", key));
            }
            bytesRedisTemplate.delete(key);
        }catch (Exception e){
            log.error(String.format("删除redis失败,key=%s", key), e);
        }
    }

    /**
     * 自增
     * @param key
     * @return
     */
    public Long increment(String key){
        try{
            if(StringUtils.isNotBlank(key)){
                log.warn(String.format("自增key失败，检查到key为空，key=%s", key));
            }
            return bytesRedisTemplate.incr(key);
        }catch (Exception e){
            log.error(String.format("redis自增失败,key=%s", key), e);
        }
        return 0L;
    }

    public void setExpire(String key, long expireTime){
        try{
            if(StringUtils.isNotBlank(key)){
                log.warn(String.format("设置过期时间失败，检查到key为空，key=%s", key));
            }
            bytesRedisTemplate.setExpire(key, expireTime);
        }catch (Exception e){
            log.error(String.format("redis设置过期时间失败,key=%s", key), e);
        }
    }
}
