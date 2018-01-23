package io.cess.sample;

/**
 * Created by lin on 9/1/16.
 */

import java.util.Arrays;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
//import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import io.cess.sample.rs.service.JsonProvider;
import io.cess.sample.rs.service.hello1.HelloServiceImpl1;
import io.cess.sample.rs.service.hello2.HelloServiceImpl2;


//@Configuration
//public class JaxrsConfig {
//
//    @Autowired
//    private Bus bus;
//
//    @Bean
//    public Server rsServer() {
//
//        JsonProvider provider = new JsonProvider();
////        provider.setDropRootElement(true);
//
//        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
//        endpoint.setBus(bus);
//        endpoint.setServiceBeans(Arrays.<Object>asList(new HelloServiceImpl1(), new HelloServiceImpl2()));
//        endpoint.setAddress("/rest");
//
//        endpoint.setProviders(Arrays.asList(provider));
////        endpoint.setSchemaLocation("ws");
////        endpoint.setFeatures(Arrays.asList(new Swagger2Feature()));
//        return endpoint.create();
//    }
//}
