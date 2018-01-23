package io.cess.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by lin on 9/1/16.
 */
@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan(value = {"lin"},excludeFilters = @ComponentScan.Filter(value=org.springframework.stereotype.Controller.class,type= FilterType.ANNOTATION))
@ComponentScan(value = "io.cess")
public class SampleApplication {//extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleApplication.class, args);
//        SpringApplication.run(new String[]{"classpath*:META-INF/lin/lin-spring-bean.xml","classpath*:META-INF/lin/lin-cxf-bean.xml"},args);
    }

//    @Autowired
//    private WebMvcProperties webMvcProperties;

//    @Bean
//    @ConditionalOnMissingBean(InternalResourceViewResolver.class)
//    InternalResourceViewResolver internalResourceViewResolver () {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix(webMvcProperties.getView().getPrefix());
//        viewResolver.setSuffix(webMvcProperties.getView().getSuffix());
//        return viewResolver;
//    }
}
