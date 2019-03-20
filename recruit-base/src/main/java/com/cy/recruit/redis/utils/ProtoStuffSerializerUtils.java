package com.cy.recruit.redis.utils;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author chenyu23
 * @Description 序列化反序列化工具类
 */
@Slf4j
public class ProtoStuffSerializerUtils {
	
	/**
	 * 序列化对象obj
	 * @param obj
	 * @return
	 */
	public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            return null;
        }
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
        	log.error("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!" , e);
            throw new RuntimeException("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!", e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

	/**
	 * 反序列化对象
	 * @param paramArrayOfByte	对象字节码
	 * @param targetClass		反序列化类class对象
	 * @return
	 */
    public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            return null;
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
        	log.error("反序列化过程中依据类型创建对象失败!", e);
            throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e);
        }
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
        return instance;
    }
    
    /**
     * 序列化列表list
     * @param objList 序列化列表objList
     * @return
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            return null;
        }
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
        	log.error("序列化对象列表(" + objList + ")发生异常!" , e);
            throw new RuntimeException("序列化对象列表(" + objList + ")发生异常!", e);
        } finally {
            buffer.clear();
            try {
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return protostuff;
    }
    
    /**
     * 反序列化列表
     * @param paramArrayOfByte	列表字节码
     * @param targetClass		列表内对象class对象
     * @return
     */
    public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            return null;
        }
        
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
        } catch (IOException e) {
        	log.error("反序列化对象列表发生异常!" , e);
            throw new RuntimeException("反序列化对象列表发生异常!",e);
        }
        return result;
    }
}
