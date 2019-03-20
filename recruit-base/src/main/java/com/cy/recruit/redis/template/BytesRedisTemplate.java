package com.cy.recruit.redis.template;

import com.cy.recruit.redis.utils.ProtoStuffSerializerUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redistemplate ， 操作redis
 * @author chenyu23
 * @Description 对redis做操作
 */
@Component
public class BytesRedisTemplate {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 设置key的过期时间
     * @param key
     * @param timeout
     * @return
     */
    public boolean setExpire(String key , long timeout){
        return redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置key的过期时间
     * @param key
     * @return
     */
    public boolean setExpire(String key){
        return setExpire(key , 0);
    }

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     * @param key
     * @param bvalue
     * @param expireTime
     */
    public void setEx(String key, final byte[] bvalue, final long expireTime) {
        final byte[] bkey = key.getBytes();
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(bkey, expireTime, bvalue);
                return true;
            }
        });
    }

    /**
     * 从redis中获取字节码对象，并反序列话成Java对象
     * @param key
     * @return
     */
    public byte[] get(final String key) {
        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
        if (result == null) {
            return null;
        }
        return result;
    }

    /**
     * 精确删除key
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 自增
     * @param key
     */
    public Long incr(String key){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.incr(key.getBytes());
            }
        });
    }

    /**
     * 模糊删除key
     * @param pattern
     */
    public void keys(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 从redis列表末尾插入value对象
     * @param key
     * @return 插入对象列表
     */
    public Long lPush(String key , final byte[]... bvalues){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lPush(bkey, bvalues);
            }
        });
    }

    /**
     * 从redis列表头部插入value对象
     * @param key
     * @param bvalues
     * @return 插入对象列表
     */
    public Long rPush(String key , final byte[]... bvalues){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.rPush(bkey, bvalues);
            }
        });
    }


    /**
     * 从列表中移除最后一个元素，并返回
     * @param key
     * @return 移除的对象
     */
    public byte[] lPop(String key){
        final byte[] bkey = key.getBytes();
        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lPop(bkey);
            }
        });
        return result;
    }

    /**
     * 从列表中移除最后一个元素，并返回
     * @param key
     * @return 移除的对象
     */
    public List<Object> lPopPipeLine(String key, final Long count){
        final byte[] bkey = key.getBytes();
        List<Object> result = redisTemplate.executePipelined(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                for(int i=0 ;i<count; i++){
                    connection.lPop(bkey);
                }
                return connection.closePipeline();
            }
        });
        return result;
    }

    /**
     * 从列表中移除最后一个元素，并返回
     * @param key
     * @return 移除的对象
     */
    public List<Object> rPopPipeLine(String key, final Long count){
        final byte[] bkey = key.getBytes();
        List<Object> result = redisTemplate.executePipelined(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                for(int i=0 ;i<count; i++){
                    connection.rPop(bkey);
                }
                return connection.closePipeline();
            }
        });
        return result;
    }

    /**
     * 从列表中移除第一个元素，并返回
     * @param key
     * @return 移除的对象
     */
    public byte[] rPop(String key){
        final byte[] bkey = key.getBytes();
        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.rPop(bkey);
            }
        });
        return result;
    }

    /**
     * 从redis列表移除前count个值为value的元素
     * @param key
     * @param count
     * @param bvalue
     * @return
     */
    public Long lRem(String key , final long count , final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lRem(bkey , count ,bvalue);
            }
        });
    }

    /**
     * 截取出start到end之间的列表
     * @param key
     * @param start
     * @param end
     */
    public void lTrim(String key , final long start ,final long end){
        final byte[] bkey = key.getBytes();
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.lTrim(bkey, start, end);
                return true;
            }
        });
    }

    /**
     * 获取List列表长度
     * @param key
     */
    public Long lLen(String key){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lLen(bkey);
            }
        });
    }

    /**
     * 更新list中索引index的值
     * @param key
     * @param index
     * @param bvalue
     */
    public void lSet(String key ,final long index, final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.lSet(bkey, index, bvalue);
                return true;
            }
        });
    }

    /**
     * 获取list中 start至end中的所有元素，超出范围，获取所有元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<byte[]> lRange(final String key , final long start ,final long end){
        final byte[] bkey = key.getBytes();
        List<byte[]> results = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lRange(bkey, start, end);
            }
        });
        if(results == null || results.size() <= 0){
            return null;
        }
        return results;
    }

    /**
     * 获取索引为index的对象
     * @param key
     * @param index
     * @return
     */
    public byte[] lIndex(String key , final long index){
        final byte[] bkey = key.getBytes();
        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lIndex(bkey, index);
            }
        });
        if(result == null){
            return null;
        }
        return result;
    }

    /**
     * set集合中插入元素，分值为score，如果该元素存在，则更新分值
     * @param key
     * @param score
     * @param bvalue
     * @return
     */
    public Boolean zAdd(String key , final double score , final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zAdd(bkey , score , bvalue);
            }
        });
    }

    /**
     * 获取set集合大小
     * @param key
     * @return
     */
    public Long zCard(String key){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zCard(bkey);
            }
        });
    }

    /**
     * 获取有序集合分数由低到高的排名
     * @param key
     * @param bvalue
     * @return
     */
    public Long zRank(String key, final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRank(bkey, bvalue);
            }
        });
    }

    /**
     * 获取有序集合分数由高到低的排名
     * @param key
     * @return
     */
    public Long zRevRank(String key, final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRevRank(bkey, bvalue);
            }
        });
    }

    /**
     * 获取分值在minScore到maxScore之间的元素个数
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Long zCount(String key ,final double minScore ,final double maxScore){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zCount(bkey, minScore, maxScore);
            }
        });
    }

    /**
     * 为对象value加increment分
     * @param key
     * @param increment
     * @param bvalue
     * @return
     */
    public Double zIncrBy(String key , final double increment ,final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Double>() {
            @Override
            public Double doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zIncrBy(bkey, increment, bvalue);
            }
        });
    }

    /**
     * 求keys集合交集，分数是所有交集元素score的和，并存入destkey
     * @param destKey
     * @param keys
     * @return
     */
    public Long zInterStore(String destKey , List<String> keys){
        if(keys == null || keys.size() <= 0){
            return null;
        }
        final byte[] bkey = destKey.getBytes();
        final byte[][] bsets = new byte[keys.size()][];
        for(int i = 0 ; i < keys.size() ;i++){
            bsets[i] = keys.get(i).getBytes();
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zInterStore(bkey, bsets);
            }
        });
    }

    /**
     * 求keys集合交集，分数从aggregate来判断是求和，最大，最小值作为分值，weight为每个集合成的系数
     * @param destKey
     * @param aggregate
     * @param weights
     * @param keys
     * @return
     */
    public Long zInterStore(String destKey , final RedisZSetCommands.Aggregate aggregate, final int[] weights, List<String> keys){
        if(keys==null || weights.length != keys.size()){
            return 0L;
        }
        final byte[] bkey = destKey.getBytes();
        final byte[][] bsets = new byte[keys.size()][];
        for(int i = 0 ; i < keys.size() ;i++){
            bsets[i] = keys.get(i).getBytes();
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zInterStore(bkey, aggregate, weights, bsets);
            }
        });
    }

    /**
     * 求sets集合交集，分数是所有交集元素score的和，并存入destkey
     * @param destKey
     * @param sets
     * @return
     */
    public Long zInterStore(String destKey , String... sets){
        return zInterStore(destKey, Arrays.asList(sets));
    }

    /**
     * 求sets集合并集，并存入destkey
     * @param destKey
     * @param keys
     * @return
     */
    public Long zUnionStore(String destKey , List<String> keys){
        if(keys == null || keys.size() <= 0){
            return null;
        }
        final byte[] bkey = destKey.getBytes();
        final byte[][] bsets = new byte[keys.size()][];
        for(int i = 0 ; i < keys.size() ;i++){
            bsets[i] = keys.get(i).getBytes();
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zUnionStore(bkey, bsets);
            }
        });
    }

    /**
     * 求sets集合并集，并存入destkey
     * @param destKey
     * @param sets
     * @return
     */
    public Long zUnionStore(String destKey , String... sets){
        return zUnionStore(destKey, Arrays.asList(sets));
    }

    /**
     * 求sets集合并集，分数从aggregate来判断是求和，最大，最小值作为分值，weight为每个集合成的系数
     * @param destKey
     * @param aggregate
     * @param weights
     * @return
     */
    public Long zUnionStore(String destKey , final RedisZSetCommands.Aggregate aggregate, final int[] weights, List<String> keys){
        if(keys == null || weights.length != keys.size()){
            return null;
        }
        final byte[] bkey = destKey.getBytes();
        final byte[][] bsets = new byte[keys.size()][];
        for(int i = 0 ; i < keys.size() ;i++){
            bsets[i] = keys.get(i).getBytes();
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zUnionStore(bkey, aggregate, weights, bsets);
            }
        });
    }

    /**
     * 获取Set集合中set至end间所有元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<byte[]> zRange(String key , final long start ,final long end){
        final byte[] bkey = key.getBytes();
        Set<byte[]> sets =  redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRange(bkey, start, end);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取Set集合中set至end间所有元素，并由高到低排列
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<byte[]> zRevRange(String key , final long start ,final long end){
        final byte[] bkey = key.getBytes();
        Set<byte[]> sets =  redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRevRange(bkey, start, end);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取分值在minScore至maxScore之间的所有元素
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<byte[]> zRangeByScore(String key , final double minScore ,final double maxScore){
        final byte[] bkey = key.getBytes();
        Set<byte[]> sets =  redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRangeByScore(bkey, minScore, maxScore);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取分值在minScore至maxScore之间的所有元素，并由高到低排列
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<byte[]> zRevRangeByScore(String key , final double minScore ,final double maxScore){
        final byte[] bkey = key.getBytes();
        Set<byte[]> sets =  redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRevRangeByScore(bkey, minScore, maxScore);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取Set集合中set至end间所有元素, Tuple包含值和分数
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(String key , final long start , final long end){
        final byte[] bkey = key.getBytes();
        Set<RedisZSetCommands.Tuple> sets =  redisTemplate.execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() {
            @Override
            public Set<RedisZSetCommands.Tuple> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRangeWithScores(bkey, start, end);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取Set集合中set至end间所有元素，并由高到低排列
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(String key , final long start , final long end){
        final byte[] bkey = key.getBytes();
        Set<RedisZSetCommands.Tuple> sets =  redisTemplate.execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() {
            @Override
            public Set<RedisZSetCommands.Tuple> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRevRangeWithScores(bkey, start, end);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取分值在minScore至maxScore之间的所有元素
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(String key , final double minScore , final double maxScore){
        final byte[] bkey = key.getBytes();
        Set<RedisZSetCommands.Tuple> sets =  redisTemplate.execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() {
            @Override
            public Set<RedisZSetCommands.Tuple> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRangeByScoreWithScores(bkey, minScore, maxScore);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 获取分值在minScore至maxScore之间的所有元素，并由高到低排列
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(String key , final double minScore , final double maxScore){
        final byte[] bkey = key.getBytes();
        Set<RedisZSetCommands.Tuple> sets =  redisTemplate.execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() {
            @Override
            public Set<RedisZSetCommands.Tuple> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRevRangeByScoreWithScores(bkey, minScore, maxScore);
            }
        });
        if(sets == null || sets.size() <= 0){
            return null;
        }
        return sets;
    }

    /**
     * 移除values的所有元素
     * @param key
     * @param bvalues
     * @return
     */
    public Long zRem(String key , byte[]... bvalues){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRem(bkey, bvalues);
            }
        });
    }

    /**
     * 移除start至end的所有角色
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zRemRange(String key , final long start , final long end){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRemRange(bkey, start , end);
            }
        });
    }

    /**
     * 移除分值为minScore至maxScore之间的元素
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Long zRemRangeByScore(String key, final double minScore ,final double maxScore){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zRemRangeByScore(bkey, minScore , maxScore);
            }
        });
    }

    /**
     * 获取value对应的分值
     * @param key
     * @param bvalue
     * @return
     */
    public Double zScore(String key ,final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Double>() {
            @Override
            public Double doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zScore(bkey, bvalue);
            }
        });
    }

    /**
     * 向Set集合中插入一个元素，如果元素大小超过限制，则移除分值最小的元素
     * @param key
     * @param limit
     * @param bvalue
     * @param score
     * @return
     */
    public Boolean zAddRemMinScore(String key , final long limit ,final byte[] bvalue , final double score){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.zAdd(bkey, score , bvalue);
                long count = connection.zCard(bkey);//元素个数
                if(count > limit){
                    connection.zRemRange(bkey, 0 , count - limit);
                }
                return true;
            }
        });
    }

    /**
     * 插入hash
     * @param key
     * @param field
     * @param bvalue
     * @return
     */
    public Boolean hSet(String key , String field , final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        final byte[] bfield = field.getBytes();
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hSet(bkey, bfield, bvalue);
            }
        });
    }

    /**
     * 不存在时插入hash，存在时不插入
     * @param key
     * @param field
     * @param bvalue
     * @return
     */
    public Boolean hSetNX(String key, String field, final byte[] bvalue){
        final byte[] bkey = key.getBytes();
        final byte[] bfield = field.getBytes();
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hSetNX(bkey, bfield, bvalue);
            }
        });
    }

    /**
     * 从hash中获取对象
     * @param key
     * @param field
     * @return
     */
    public byte[] hGet(String key , String field){
        final byte[] bkey = key.getBytes();
        final byte[] bfield = field.getBytes();
        byte[] result =  redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hGet(bkey, bfield);
            }
        });
        if(result == null){
            return null;
        }
        return result;
    }

    /**
     * 获取fields对应的对象集合列表
     * @param key
     * @param fields
     * @return
     */
    public List<byte[]> hMGet(String key, List<String> fields){
        if(fields==null || fields.size()<=0){
            return null;
        }
        final byte[] bkey = key.getBytes();
        final byte[][] bfields = new byte[fields.size()][];
        for(int index = 0; index < fields.size() ; index++){
            bfields[index] = fields.get(index).getBytes();
        }
        List<byte[]> result =  redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hMGet(bkey, bfields);
            }
        });
        if(result == null || result.size() <= 0){
            return null;
        }
        return result;
    }

    /**
     * 获取fields对应的对象集合列表
     * @param key
     * @param fields
     * @return
     */
    public List<byte[]> hMGet(String key, String... fields){
        return hMGet(key, Arrays.asList(fields));
    }

    /**
     * 批量插入hash
     * @param key
     * @param hashes
     */
    public void hMSet(String key, Map<String, byte[]> hashes){
        if(hashes == null || hashes.size() <= 0){
            return ;
        }
        final byte[] bkey = key.getBytes();
        final Map<byte[],byte[]> bhashes = new HashMap<byte[],byte[]>();
        for(Map.Entry<String, byte[]> entry : hashes.entrySet()){
            bhashes.put(entry.getKey().getBytes(), entry.getValue());
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hMSet(bkey, bhashes);
                return true;
            }
        });
    }

    /**
     * 获取hash的大小
     * @param key
     * @return
     */
    public Long hLen(String key){
        final byte[] bkey = key.getBytes();
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hLen(bkey);
            }
        });
    }

    /**
     * 判断域field是否存在
     * @param key
     * @param field
     * @return
     */
    public Boolean hExists(String key , String field){
        final byte[] bkey = key.getBytes();
        final byte[] bfield = field.getBytes();
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hExists(bkey ,bfield);
            }
        });
    }

    /**
     * 从hash中删除键为fields集合中的所有元素
     * @param key
     * @param fields
     * @return
     */
    public Long hDel(String key, List<String> fields){
        if(fields == null || fields.size() <=0 ){
            return 0L;
        }
        final byte[] bkey = key.getBytes();
        final byte[][] bfields = new byte[fields.size()][];
        for(int index = 0 ; index < fields.size() ;index ++){
            bfields[index] = fields.get(index).getBytes();
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hDel(bkey , bfields);
            }
        });
    }

    /**
     * 删除所有键在fields内的元素
     * @param key
     * @param fields
     * @return
     */
    public Long hDel(String key , String... fields){
        return hDel(key , Arrays.asList(fields));
    }

    /**
     * 获取hash所有key
     * @return
     */
    public Set<byte[]> hKeys(String key){
        final byte[] bkey = key.getBytes();
        Set<byte[]> bkeys =  redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hKeys(bkey);
            }
        });
        if(bkeys == null || bkeys.size() <= 0){
            return null;
        }
        return bkeys;
    }

    /**
     * 获取hash所有value
     * @return
     */
    public List<byte[]> hVals(String key){
        final byte[] bkey = key.getBytes();
        List<byte[]> bvalues =  redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hVals(bkey);
            }
        });
        if(bvalues == null || bvalues.size() <= 0){
            return null;
        }
        return bvalues;
    }

    /**
     * 获取所有hash
     * @param key
     * @return
     */
    public Map<String, byte[]> hGetAll(final String key){
        Map<byte[],byte[]> bresult = redisTemplate.execute(new RedisCallback<Map<byte[],byte[]>>() {
            @Override
            public Map<byte[],byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hGetAll(key.getBytes());
            }
        });
        if(bresult == null || bresult.size() <= 0){
            return null;
        }
        Map<String , byte[]> result = new HashMap<String, byte[]>();
        if(bresult != null && bresult.size() > 0){
            for(Map.Entry<byte[], byte[]> entry : bresult.entrySet()){
                result.put(ProtoStuffSerializerUtils.deserialize(entry.getKey(), String.class), entry.getValue());
            }
        }
        return result;
    }

}

