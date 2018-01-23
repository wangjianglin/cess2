package io.cess.core.cxf;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.spring.SpringComponentScanServer;
import org.apache.cxf.jaxrs.spring.SpringJaxrsClassesScanServer;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;

@Configuration
@ConditionalOnProperty("io.cess.cxf")
@ConditionalOnWebApplication
@ConditionalOnClass({ SpringBus.class, CXFServlet.class })
@EnableConfigurationProperties(CxfProperties.class)
@AutoConfigureAfter(name = {
        "org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration", // Spring Boot 1.x
        "org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration" // Spring Boot 2.x
})
public class CxfConfig {

    @Autowired
    private CxfProperties properties;

    @Bean
    @ConditionalOnMissingBean(name = "cxfServletRegistration")
    public ServletRegistrationBean cxfServletRegistration() {
        String path = this.properties.getPath();
        String urlMapping = path.endsWith("/") ? path + "*" : path + "/*";
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new CXFServlet(), urlMapping);
        CxfProperties.Servlet servletProperties = this.properties.getServlet();
        registration.setLoadOnStartup(servletProperties.getLoadOnStartup());
        for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
            registration.addInitParameter(entry.getKey(), entry.getValue());
        }
        return registration;
    }

    @Configuration
    @ConditionalOnProperty("io.cess.cxf")
    @ConditionalOnMissingBean(SpringBus.class)
    @ImportResource("classpath:META-INF/cxf/cxf.xml")
    protected static class SpringBusConfiguration {

    }

    @Configuration
    @ConditionalOnProperty("io.cess.cxf")
    @ConditionalOnClass(JAXRSServerFactoryBean.class)
    @ConditionalOnExpression("'${cxf.jaxrs.component-scan}'=='true' && '${cxf.jaxrs.classes-scan}'!='true'")
    @Import(SpringComponentScanServer.class)
    protected static class JaxRsComponentConfiguration {

    }

    @Configuration
    @ConditionalOnProperty("io.cess.cxf")
    @ConditionalOnClass(JAXRSServerFactoryBean.class)
    @ConditionalOnExpression("'${cxf.jaxrs.classes-scan}'=='true' && '${cxf.jaxrs.component-scan}'!='true'")
    @Import(SpringJaxrsClassesScanServer.class)
    protected static class JaxRsClassesConfiguration {

    }


    @Bean
    @ConditionalOnProperty("io.cess.cxf")
    @ConditionalOnMissingBean(Jsr181HandlerMapping.class)
    public Jsr181HandlerMapping webService(){
        Jsr181HandlerMapping handler = new Jsr181HandlerMapping();
        handler.setUrlPrefix("/");
        return  handler;
    }

    @Bean
    @ConditionalOnProperty("io.cess.cxf")
    @ConditionalOnMissingBean(RestHandlerMapping.class)
    public RestHandlerMapping restServer(){
        return new RestHandlerMapping();
    }


}
