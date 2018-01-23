//package lin.core;
//
//import org.springframework.beans.MutablePropertyValues;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.bind.RelaxedDataBinder;
////import org.springframework.boot.bind.RelaxedPropertyResolver;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.Database;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.support.TransactionTemplate;
//import org.springframework.core.env.Environment;
//
//import javax.annotation.Resource;
//import javax.persistence.EntityManagerFactory;
//import java.util.Properties;
//
///**
// * Created by lin on 27/10/2016.
// */
////@Configuration
////@EnableConfigurationProperties(JpaConfigProperties.class)
//@Import(AutoConfig.class)
//public abstract class JpaConfig {
//
//    //RelaxedDataBinder
//
//    @Autowired
//    private Environment env;
//
//    protected FactoryBean<EntityManagerFactory> entityManagerFactory(){
//        return this.entityManagerFactory(null, "lin.database.");
//    }
//    protected FactoryBean<EntityManagerFactory> entityManagerFactory(String unitNmae){
//        return this.entityManagerFactory(unitNmae, "lin.database.");
//    }
//    protected FactoryBean<EntityManagerFactory> entityManagerFactory(String unitName, String prefix){
//
////        if(prefix == null || "".equals(prefix)){
////            prefix = "lin.database.";
////        }
////        if(!prefix.endsWith(".")){
////            prefix = prefix + ".";
////        }
////        RelaxedPropertyResolver properties = new RelaxedPropertyResolver(env);
////
////        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
////        emf.setPackagesToScan(properties.getProperty(prefix+"packages_scan","lin.*"));
////
////        if(unitName == null) {
////            emf.setPersistenceUnitName(properties.getProperty(prefix + "unitName"));
////        }else{
////            emf.setPersistenceUnitName(unitName);
////        }
////
////
////        Properties jpaProperties = new Properties();
////
////        jpaProperties.putAll(properties.getSubProperties(prefix));
////
////
//////        jpaProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
//////        jpaProperties.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
//////        jpaProperties.setProperty("hibernate.connection.username","root");
//////        jpaProperties.setProperty("hibernate.connection.password","");
//////        jpaProperties.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/test");
//////        jpaProperties.setProperty("hibernate.hbm2ddl.auto","update");
//////        jpaProperties.setProperty("hibernate.show_sql","true");
//////        jpaProperties.setProperty("hibernate.format_sql","true");
//////
//////
//////
//////        jpaProperties.setProperty("hibernate.connection.provider_class",properties.getProperty("hibernate.connection.provider_class","org.hibernate.connection.C3P0ConnectionProvider"));
//////        jpaProperties.setProperty("hibernate.c3p0.testConnectionOnCheckout",properties.getProperty("hibernate.c3p0.testConnectionOnCheckout","true"));
//////        jpaProperties.setProperty("c3p0.min_size",properties.getProperty("c3p0.min_size","5"));
//////        jpaProperties.setProperty("c3p0.max_size",properties.getProperty("c3p0.max_size","30"));
//////        jpaProperties.setProperty("c3p0.maxIdleTime",properties.getProperty("c3p0.maxIdleTime","60"));
//////        jpaProperties.setProperty("c3p0.timeout",properties.getProperty("c3p0.timeout","1800"));
//////        jpaProperties.setProperty("c3p0.max_statements",properties.getProperty("c3p0.max_statements","50"));
//////        jpaProperties.setProperty("c3p0.idle_test_period",properties.getProperty("c3p0.idle_test_period","120"));
//////        jpaProperties.setProperty("c3p0.acquire_increment",properties.getProperty("c3p0.acquire_increment","1"));
//////        jpaProperties.setProperty("c3p0.validate",properties.getProperty("c3p0.validate","false"));
////
////
////        emf.setJpaProperties(jpaProperties);
////
////
////        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
////        jpaVendorAdapter.setDatabase(Database.MYSQL);
////        emf.setJpaVendorAdapter(jpaVendorAdapter);
////
////        return emf;
//        return null;
//    }
////    public LocalContainerEntityManagerFactoryBean emf(){
////        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
////        emf.setPackagesToScan("lin.*");
////        emf.setPersistenceUnitName(this.unitName);
////        Properties jpaProperties = new Properties();
////        jpaProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
////        jpaProperties.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
////        jpaProperties.setProperty("hibernate.connection.username","root");
////        jpaProperties.setProperty("hibernate.connection.password","");
//////        jpaProperties.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/test");
////        jpaProperties.setProperty("hibernate.connection.url",this.url);
////        jpaProperties.setProperty("hibernate.hbm2ddl.auto","update");
////        jpaProperties.setProperty("hibernate.show_sql","true");
////        jpaProperties.setProperty("hibernate.format_sql","true");
////        emf.setJpaProperties(jpaProperties);
////
////        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
////        jpaVendorAdapter.setDatabase(Database.MYSQL);
////        emf.setJpaVendorAdapter(jpaVendorAdapter);
////
////        return emf;
////    }
//}
