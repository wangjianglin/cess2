//package io.cess.gateway.config;
//
//import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
//import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//public class Config {
//
////    @Bean
////    public PatternServiceRouteMapper patternServiceRouteMapper(){
////        return new PatternServiceRouteMapper(
//////                "(?<name>^.+)-(?<version>v.+$)",
////                ".*?(?<!/)/(?<name>.*)",
////                "api/${name}"
////        );
////    }
//
//    @Bean
//    @Primary
//    public ProxyRequestHelper proxyRequestHelper(){
//        return new CessProxyRequestHelper();
//    }
//}
