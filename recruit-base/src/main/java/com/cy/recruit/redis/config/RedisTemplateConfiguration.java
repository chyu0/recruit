package com.cy.recruit.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@Configuration
public class RedisTemplateConfiguration {

    @Resource
    private RedisSettingProperties redisSettingProperties;

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        RedisSettingProperties.Pool pool = redisSettingProperties.getPool();
        if(pool != null){
            config.setMaxTotal(pool.getMaxTotal());//最大连接数
            config.setMaxIdle(pool.getMaxIdle());//最大空闲连接数
            config.setMinIdle(pool.getMinIdle());//最小空闲数
            config.setMaxWaitMillis(pool.getMaxWaitMillis());//最大阻塞等待时间
            config.setTestOnBorrow(pool.isTestOnBorrow());//获取连接检查，无效移除
            config.setTestOnReturn(pool.isTestOnReturn());//归还检查连接有效性
            config.setMinEvictableIdleTimeMillis(pool.getMinEvictableIdleMillis());//连接最小空闲等待时间
            config.setSoftMinEvictableIdleTimeMillis(pool.getSoftMinEvictableIdleMillis());//最小空闲时间，并保留minidle个空闲连接
            config.setTimeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRunsMillis());
            config.setTestWhileIdle(pool.isTestWhileIdle());
            config.setNumTestsPerEvictionRun(pool.getNumTestsPerEvictionRun());
            config.setBlockWhenExhausted(pool.isBlockWhenExhausted());//等待超时是否阻塞
        }
        return config;
    }

    /**
     * jedis连接工厂
     * @param jedisPoolConfig
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(redisSettingProperties.getHost());
        redisStandaloneConfiguration.setPort(redisSettingProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisSettingProperties.getPass());
        //获得默认的连接池构造
        //这里需要注意的是，JedisConnectionFactory对于Standalone模式的没有（RedisStandaloneConfiguration，JedisPoolConfig）的构造函数，对此
        //我们用JedisClientConfiguration接口的builder方法实例化一个构造器，还得类型转换
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //修改我们的连接池配置
        jpcf.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcf.build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        return RedisCacheManager.create(connectionFactory);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
