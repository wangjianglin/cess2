//package io.cess.gateway.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@ConfigurationProperties("zuul")
//public class ZuulExtProperties {
//
//    private Map<String, ZuulRoute> routes = new LinkedHashMap<>();
//
//    public Map<String, ZuulRoute> getRoutes() {
//        return routes;
//    }
//
//    public void setRoutes(Map<String, ZuulRoute> routes) {
//        this.routes = routes;
//    }
//
//    public static class ZuulRoute{
//
//        private String prefix;
//
//        public String getPrefix() {
//            return prefix;
//        }
//
//        public void setPrefix(String prefix) {
//            this.prefix = prefix;
//        }
//    }
//}
