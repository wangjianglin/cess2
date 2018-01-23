package io.cess.sample;


//import lin.core.JpaConfig;
import io.cess.core.JpaConfigProperties;
import io.cess.core.cxf.Jsr181HandlerMapping;
import io.cess.core.cxf.RestHandlerMapping;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Created by lin on 9/22/16.
 */
//
//public class WebServiceConfig {
//    @Autowired
//    private Bus bus;
//
//
@Configuration
//@EnableConfigurationProperties(JpaConfigProperties.class)
//@AutoConfigureAfter(JpaConfigProperties.class)
//@ImportResource(locations={"classpath*:META-INF/lin/lin-spring-bean.xml"})
public class DemoConfig{//} extends JpaConfig {

//    @Autowired()
//    private JpaConfigProperties properties;

    @Autowired
    private JpaProperties jpaProperties;

//    @Bean
////    @ConditionalOnClass(Jsr181HandlerMapping.class)
//    public Jsr181HandlerMapping webService(){
//        Jsr181HandlerMapping handler = new Jsr181HandlerMapping();
//        handler.setUrlPrefix("/webs/");
//        return  handler;
//    }

//    @Bean
//    @ConditionalOnClass(Jsr181HandlerMapping.class)
//    public RestHandlerMapping restServer(){
//        return new RestHandlerMapping();
//    }


//    @Autowired
//    Environmentent env;

//    public DemoConfig(JpaConfigProperties properties){
//        this.properties = properties;
//    }


//    @Bean
//    public PropertyPlaceholderConfigurer propertyConfigurer(){
//        PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
//        propertyConfigurer.setOrder(Integer.MAX_VALUE);
//
//        propertyConfigurer.setLocations(new ClassPathResource("META-INF/lin/lin-default.properties"),
//                new ClassPathResource("META-INF/lin/lin.properties"));
//        return propertyConfigurer;
//    }

//    @Bean
//    @Primary
//    public JpaTransactionManager jpaTransactionManager() throws Exception {
//        JpaTransactionManager jta = new JpaTransactionManager();
//        jta.setEntityManagerFactory(emf().getObject());
//        return jta;
//    }
//
//    @Bean
//    @Primary
//    public TransactionTemplate transactionTemplate() throws Exception {
//        TransactionTemplate template = new TransactionTemplate();
//        template.setTimeout(300);
//        template.setTransactionManager(jpaTransactionManager());
//        return template;
//    }
//
//    @Bean()
//    @Primary
//    public EntityManager entityManager() throws Exception {
//        return emf().getObject().createEntityManager();
//    }
//
//    @Bean(name = "emf")
//    public FactoryBean<EntityManagerFactory> emf(){
//        return this.entityManagerFactory();
//    }
}
