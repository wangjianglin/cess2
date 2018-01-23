package io.cess.core.jpa;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


//采用动态注册bean 的方式
public abstract class JpaConfig {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private ConversionService conversionService;

    private String persistenceUnit;
    private String[] packages;

    protected JpaConfig(String persistenceUnit,String ... packages) {
        this.persistenceUnit = persistenceUnit;
        this.packages = packages;
        System.setProperty("hibernate.dialect.storage_engine","innodb");//innodb、myisam
    }

    protected DataSource createDataSource(){

        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    public abstract DataSource dataSource();

    protected EntityManager createEntityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

    public abstract EntityManager entityManager(EntityManagerFactoryBuilder builder);

    public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory (EntityManagerFactoryBuilder builder);

//    @Bean(name = "entityManagerFactorySecondary")
    protected LocalContainerEntityManagerFactoryBean createEntityManagerFactory (EntityManagerFactoryBuilder builder) {
//        DataSource dataSource = (DataSource) beanFactory.getBean("secondaryDS");
        return builder
                .dataSource(dataSource())
                .properties(getVendorProperties())
                .packages(this.packages)
                .persistenceUnit(this.persistenceUnit)
                .build();
    }

    protected Map<String,?> getVendorProperties(){
        return new HashMap<>();
    }

    public abstract PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder);

    protected PlatformTransactionManager createTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }
}
