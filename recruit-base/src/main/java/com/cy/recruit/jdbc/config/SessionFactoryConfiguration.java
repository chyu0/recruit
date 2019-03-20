package com.cy.recruit.jdbc.config;

import com.cy.recruit.jdbc.setting.MybatisSettingProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class SessionFactoryConfiguration implements TransactionManagementConfigurer {

    @Resource
    private DataSource dataSource;

    @Resource(name = "mybatisSettingProperties")
    private MybatisSettingProperties mybatisSetting;

    private String CLASS_PATH = "classpath:";

    /**
      * 创建sqlSessionFactoryBean 实例 并且设置configtion 如驼峰命名.等等 设置mapper 映射路径
      * 设置datasource数据源
      *
      * @return
      * @throws Exception
      */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        /** 设置mybatis configuration 扫描路径 */
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisSetting.getConfigPath()));
        /** 设置datasource */
        sqlSessionFactoryBean.setDataSource(dataSource);
        /** 设置typeAlias 包扫描路径 */
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisSetting.getTypeAlias());
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(CLASS_PATH + mybatisSetting.getMapperPath()));
        return sqlSessionFactoryBean;
    }
    
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
