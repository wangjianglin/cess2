package io.cess.core.spring;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Properties;

/**
 * Created by lin on 9/22/16.
 */
public class Configuration {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer(){
        PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
        propertyConfigurer.setOrder(Integer.MAX_VALUE);

        propertyConfigurer.setLocations(new ClassPathResource("classpath*:META-INF/lin/lin-default.properties"),
                new ClassPathResource("classpath*:META-INF/lin/lin.properties"));
        return propertyConfigurer;
    }

//    @Bean
//    public JpaTransactionManager jpaTransactionManager(){
//        JpaTransactionManager jta = new JpaTransactionManager();
//        jta.setEntityManagerFactory(emf().getObject());
//        return jta;
//    }
//
//    @Bean
//    public TransactionTemplate TransactionTemplate(){
//        TransactionTemplate template = new TransactionTemplate();
//        template.setTimeout(300);
//        template.setTransactionManager(jpaTransactionManager());
//        return template;
//    }
//
//    @Bean(name="emf")
//    public LocalContainerEntityManagerFactoryBean emf(){
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setPackagesToScan("lin.*");
//        emf.setPersistenceUnitName("default");
//        Properties jpaProperties = new Properties();
//        jpaProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
//        jpaProperties.setProperty("hibernate.connection.driver_class","");
//        jpaProperties.setProperty("hibernate.connection.username","root");
//        jpaProperties.setProperty("hibernate.connection.password","com.mysql.jdbc.Driver");
//        jpaProperties.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/test");
//        jpaProperties.setProperty("hibernate.hbm2ddl.auto","update");
//        jpaProperties.setProperty("hibernate.show_sql","true");
//        jpaProperties.setProperty("hibernate.format_sql","");
//        emf.setJpaProperties(jpaProperties);
//
//        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        jpaVendorAdapter.setDatabase(Database.MYSQL);
//        emf.setJpaVendorAdapter(jpaVendorAdapter);
//
//        return emf;
//    }
}
