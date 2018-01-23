package io.cess.sample;

import io.cess.core.jpa.JpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        entityManagerFactoryRef="entityManagerFactorySecondary",
        transactionManagerRef="transactionManagerSecondary",
        basePackages= { "io.cess.sample.repository2" })
public class RepositorySecondaryConfig extends JpaConfig {

    public RepositorySecondaryConfig() {
        super("secondaryPersistenceUnit", "io.cess.sample");
    }

    @Bean(name = "secondaryDS")
    @Qualifier("secondaryDS")
    @ConfigurationProperties(prefix="lin.secondary.datasource")
    public DataSource dataSource(){
        return this.createDataSource();
    }

    @Bean(name = "entityManagerSecondary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return this.createEntityManager(builder);
    }

    @Bean(name = "entityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (EntityManagerFactoryBuilder builder) {
        return this.createEntityManagerFactory(builder);
    }

    @Bean(name = "transactionManagerSecondary")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return this.createTransactionManager(builder);
    }

}