package io.cess.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class Config {

    @Autowired
    protected ServerProperties server;

    @Autowired(required = false)
    private Registration registration;

    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    private ServiceRouteMapper serviceRouteMapper;


    @Autowired
    protected ZuulProperties zuulProperties;
//    @Bean
//    public PatternServiceRouteMapper patternServiceRouteMapper(){
//        return new PatternServiceRouteMapper(
////                "(?<name>^.+)-(?<version>v.+$)",
//                ".*?(?<!/)/(?<name>.*)",
//                "api/${name}"
//        );
//    }

//    @Bean
//    @Primary
//    public ProxyRequestHelper proxyRequestHelper(){
//        return new CessProxyRequestHelper();
//    }
    @Bean
    public DiscoveryClientRouteLocator discoveryRouteLocator() {
        return new DiscoveryClientRouteLocator(this.server.getServlet().getServletPrefix(), this.discovery, this.zuulProperties,
                this.serviceRouteMapper, this.registration){
            @Override
            public Route getMatchingRoute(String path) {
                Route route = super.getMatchingRoute(path);
                if("/v2/api-docs".equals(route.getPath())){
                    return route;
                }
                route.setPath(("/api/"+route.getPath()).replaceAll("//","/"));

                return route;
            }
        };
    }
}
