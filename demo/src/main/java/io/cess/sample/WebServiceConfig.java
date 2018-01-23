package io.cess.sample; /**
 * Created by lin on 9/1/16.
 */
import javax.xml.ws.Endpoint;

import io.cess.core.cxf.RestHandlerMapping;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.cess.sample.ws.service.HelloPortImpl;


//@Configuration
//public class WebServiceConfig {
//    @Autowired
//    private Bus bus;
//
//    @Bean
//    public Endpoint endpoint2() {
//        EndpointImpl endpoint = new EndpointImpl(bus, new HelloPortImpl());
//        endpoint.publish("/sample.Hello");
//        endpoint.setAddress("/ws");
//        return endpoint;
//    }
//
//    @Bean
//    public Object endpoint(){
////        EndpointImpl endpoint = new EndpointImpl(bus, new HelloPortImpl());
////        endpoint.publish("/sample.Hello");
////        endpoint.setAddress("/ws");
////        return endpoint;
//        return new RestHandlerMapping();
//    }
//}