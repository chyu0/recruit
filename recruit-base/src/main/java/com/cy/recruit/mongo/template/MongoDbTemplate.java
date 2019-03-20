package com.cy.recruit.mongo.template;

import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;

/**
 * mongodb基类
 * @author chenyu23
 * @data 2018年12月19日 下午19:40:14
 * @Description TODO
 */
@Slf4j
public class MongoDbTemplate {
	
	@Autowired
    private MongoTemplate mongoTemplate;

	@Setter
	private String collectionName;//集合名称
	
    /**
     * 根据条件查询集合
     *
     * @param query		查询条件
     * @return			满足条件的集合
     */
    public  <T> List<T> queryList(Query query , Class<T> entityClass){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]queryList("+ collectionName +"):" + query);
    	}
        return this.mongoTemplate.find(query , entityClass , collectionName);
    }
    
    
    
    /**
     * 通过条件查询单个实体
     *
     * @param query	查询条件
     * @return		满足条件的实体对象
     */
    public <T> T queryOne(Query query , Class<T> entityClass){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]queryOne("+ collectionName +"):" + query);
    	}
        return this.mongoTemplate.findOne(query, entityClass , collectionName);
    }
    
    
    /**
     * 根据Id从Collection中查询对象
     *
     * @param id	实体对象的Id,对应Collection中记录的_id字段. 
     *              需要说明的是,Mongdo自身没有主键自增机制.解决方法
     *              实体入库的时候,程序中为实体赋主键值.
     *              实体入库的时候,在mongodb中自定义函数实现主键自增机制.定义方法同js代码类似
     * @return
     */
    public <T> T queryById(String id , Class<T> entityClass) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        if(log.isDebugEnabled()){
        	log.debug("[Mongo Dao ]queryById("+ collectionName +"):" + query);
        }
        return this.mongoTemplate.findOne(query, entityClass , collectionName);
    }

    
    /**
     * 根据Id删除用户
     *
     * @param id	删除对象id
     */
    public <T> void deleteById(String id , Class<T> entityClass) {
        Criteria criteria = Criteria.where("_id").in(id);
        if(null!=criteria){
            Query query = new Query(criteria);
            if(log.isDebugEnabled()){
            	log.debug("[Mongo Dao ]deleteById("+ collectionName +"):" + query);
            }
            if(null!=query && this.queryOne(query , entityClass)!=null){
                this.mongoTemplate.remove(query, entityClass , collectionName);
            }
        }
    }
    
    /**
     * 删除对象
     *
     * @param t	待删除对象
     */
    public <T> void delete(T t){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]delete("+ collectionName +"):" + t);
    	}
        this.mongoTemplate.remove(t , collectionName);
    }
    
    
    /**
     * 删除对象通过条件删除
     */
    public <T> void delete(Query query , Class<T> entityClass){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]delete("+ collectionName +"):" + query);
    	}
        this.mongoTemplate.remove(query , entityClass , collectionName);
    }
    
    
    /**
     * 保存一个对象
     *
     * @param t 待保存实体对象
     * @return
     */
    public <T> void save(T t){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]save("+ collectionName +"):" + t);
    	}
        this.mongoTemplate.save(t , collectionName);
    }    
    
    
    /**
     * 更新满足条件的第一个记录
     *
     * @param query		更新条件
     * @param update	更新属性值
     */
    public <T> void updateFirst(Query query,Update update , Class<T> entityClass){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]updateFirst("+ collectionName +"):query(" + query + "),update(" + update + ")");
    	}
        this.mongoTemplate.updateFirst(query, update, entityClass , collectionName);
    }
    
    /**
     * 更新满足条件的所有记录
     *
     * @param query		更新条件
     * @param update	更新属性值
     */
    public <T> void updateMulti(Query query, Update update , Class<T> entityClass){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]updateMulti("+ collectionName +"):query(" + query + "),update(" + update + ")");
    	}
        this.mongoTemplate.updateMulti(query, update, entityClass , collectionName);
    }
    
    /**
     * 查找更新,如果没有找到符合的记录,则将更新的记录插入库中
     *
     * @param query		更新条件
     * @param update	更新属性值
     */
    public <T> void updateInser(Query query, Update update , Class<T> entityClass){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]updateInser("+ collectionName +"):query(" + query + "),update(" + update + ")");
    	}
        this.mongoTemplate.upsert(query, update, entityClass , collectionName);
    }
    
    /**
     * 通过条件进行分页查询
     *
     * @param query	查询条件
     * @param start	查询起始值 
     * @param size	查询大小
     * @return		满足条件的集合
     */
    public <T> List<T> getPage(Query query, int start, int size ,  Class<T> entityClass){
        query.skip(start);
        query.limit(size);
        if(log.isDebugEnabled()){
        	log.debug("[Mongo Dao ]queryPage("+ collectionName +"):" + query + "(" + start +"," + size +")");
        }
        List<T> lists = this.mongoTemplate.find(query, entityClass , collectionName);
        return lists;
    }
    
    
    /**
     * 根据条件查询库中符合记录的总数,为分页查询服务
     *
     * @param query		查询条件
     * @return			满足条件的记录总数
     */
    public Long getPageCount(Query query){
    	if(log.isDebugEnabled()){
    		log.debug("[Mongo Dao ]queryPageCount("+ collectionName +"):" + query);
    	}
        return this.mongoTemplate.count(query, collectionName);
    }
}
