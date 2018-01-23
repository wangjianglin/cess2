//package lin.sample;
//
//import lin.core.JpaConfig;
//import lin.core.JpaConfigProperties;
//import org.springframework.beans.MutablePropertyValues;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManager;
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by lin on 28/10/2016.
// */
//@Configuration
//@EnableTransactionManagement
////@EnableJpaRepositories(entityManagerFactoryRef="entityManagerFactoryPrimary",
////        transactionManagerRef="transactionManagerPrimary",
////        basePackages= { "lin" })//设置dao（repo）所在位置
//public class DataSourceConfig extends JpaConfig{
//
//    @Bean(name = "primaryDS") @Qualifier("primaryDS")
//    @ConfigurationProperties(prefix="spring.primary.datasource")
//    public DataSource primaryDataSource(){
//        DataSource dataSource = DataSourceBuilder.create().build();
//        return dataSource;
//    }
//
//    @Bean(name = "entityManagerPrimary")
//    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
//        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
//    }
//
//
//
//    @Bean(name = "entityManagerFactoryPrimary")
//    @ConfigurationProperties(prefix="spring.primary.datasource")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
//        DataSource dataSource = primaryDataSource();
//        return builder
//                .dataSource(dataSource)
////                .properties(getVendorProperties())
//                .packages("lin") //设置实体类所在位置
////                .persistenceUnit("one")
//                .build();
//    }
//
//    private Map<String, String> getVendorProperties() {
//        Map<String, String> p = new HashMap<>();
//        p.put("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
//        p.put("hibernate.hbm2ddl.auto","update");
//        p.put("hibernate.show_sql","true");
//        p.put("hibernate.format_sql","true");
//        return p;
//    }
//
//    @Bean(name = "transactionManagerPrimary")
//    PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
//        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
//    }
//}
