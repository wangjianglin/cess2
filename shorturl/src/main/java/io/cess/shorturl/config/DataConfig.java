package io.cess.shorturl.config;

import io.cess.core.jpa.JpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactory",
        transactionManagerRef="transactionManager",
        basePackages= { "io.cess.shorturl.repository" })
//@Order(Integer.MIN_VALUE)
//public class DataConfig extends JpaConfig {
public class DataConfig{
    public DataConfig() {
        System.setProperty("hibernate.dialect.storage_engine","innodb");//innodb、myisam
//        super("persistenceUnit", "io.cess.auth.entity");
    }

    @Autowired
    private JpaProperties jpaProperties;

//    @Override
    protected Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings().ddlAuto(""));
    }


    @Bean()
//    @Qualifier("datasource")
    @Primary
    @ConfigurationProperties(prefix="io.cess.datasource")
    public DataSource dataSource(){
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "entityManager")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource())
                .properties(getVendorProperties())
                .packages("io.cess.shorturl.entity")
                .persistenceUnit("persistenceUnit")
                .build();
        //"persistenceUnit", "io.cess.auth.entity"
    }

    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }

}
