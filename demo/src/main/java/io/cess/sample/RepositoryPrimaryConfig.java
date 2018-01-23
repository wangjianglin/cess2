package io.cess.sample;

import com.zaxxer.hikari.HikariConfig;
import io.cess.sample.rs.service.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
//@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryPrimary",
        transactionManagerRef="transactionManagerPrimary",
        basePackages= { "io.cess.sample" })//设置dao（repo）所在位置
//@AutoConfigureAfter(DataConfig.class)
public class RepositoryPrimaryConfig {

    HikariConfig a;
//    @Autowired
//    private JpaProperties jpaProperties;

//    @Autowired
//    @Qualifier("primaryDS")
//    private DataSource primaryDS;


//    @Bean
//    @Primary
//    @ConfigurationProperties("lin.primary.datasource")
//    public DataSourceProperties primaryDataSourceProperties() {
//        DataSourceProperties properties = new DataSourceProperties();
//        return properties;
//    }

    @Bean(name = "primaryDS")
    @Qualifier("primaryDS")
    @Primary
    @ConfigurationProperties(prefix="lin.primary.datasource")
    public DataSource primaryDataSource(){
//        DataSource dataSource = primaryDataSourceProperties().initializeDataSourceBuilder().build();
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }


//    @Bean(name = "entityManagerPrimary")
//    @Primary
//    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
//        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
//    }

    @Bean(name = "entityManagerFactoryPrimary")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(primaryDataSource())
//                .properties(getVendorProperties(primaryDataSource()))
                .packages("io.cess.sample") //设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

//    private Map<String, String> getVendorProperties(DataSource dataSource) {
//        return jpaProperties.getHibernateProperties("primary");
//    }

    @Bean(name = "transactionManagerPrimary")
    @Primary
    public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());

        transactionManager.setDataSource(this.primaryDataSource());
        transactionManager.setPersistenceUnitName("primaryPersistenceUnit");

        return transactionManager;
    }

}
