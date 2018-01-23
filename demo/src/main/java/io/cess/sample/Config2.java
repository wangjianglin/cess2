package io.cess.sample;

//import lin.core.JpaConfig;
import io.cess.core.JpaConfigProperties;
import io.cess.core.cxf.Jsr181HandlerMapping;
import io.cess.core.cxf.RestHandlerMapping;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by lin on 27/10/2016.
 */
@Configuration
//@AutoConfigureAfter(name = {
//        "lin.core.cxf.CxfConfig" // Spring Boot 1.x/ Spring Boot 2.x
//})
public class Config2 {//extends JpaConfig {


//    @Bean
//    @ConditionalOnClass(Jsr181HandlerMapping.class)
//    public Jsr181HandlerMapping webService(){
//        Jsr181HandlerMapping handler = new Jsr181HandlerMapping();
//        handler.setUrlPrefix("/webs/");
//        return  handler;
//    }
//        @Bean
////    @ConditionalOnClass(Jsr181HandlerMapping.class)
//    public RestHandlerMapping restServer(){
//            RestHandlerMapping r = new RestHandlerMapping();
//            r.setUrlPrefix("r");
//            return r;
//    }
//    @Bean(name = "oracleDS")
////    @ConfigurationProperties(prefix = "datasource.oracle")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource oracleDataSource() {
//        return DataSourceBuilder.create().build();
//    }

//    @Bean
//    public JpaTransactionManager jpaTransactionManager() throws Exception {
//        JpaTransactionManager jta = new JpaTransactionManager();
//        jta.setEntityManagerFactory(emf().getObject());
//        return jta;
//    }
//
//    @Bean
//    public TransactionTemplate transactionTemplate() throws Exception {
//        TransactionTemplate template = new TransactionTemplate();
//        template.setTimeout(300);
//        template.setTransactionManager(jpaTransactionManager());
//        return template;
//    }

//    @Bean()
//    public EntityManager entityManager() throws Exception {
//        return emf().getObject().createEntityManager();
//    }
//    @Bean(name="emf2")
////    @Qualifier("emf2")
//    //@Primary
//    public FactoryBean<EntityManagerFactory> emf(){
//        return super.entityManagerFactory("one");
//    }
}
