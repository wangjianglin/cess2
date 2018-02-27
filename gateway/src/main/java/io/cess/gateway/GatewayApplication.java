package io.cess.gateway;

import org.springframework.cloud.client.SpringCloudApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.WebApplicationType;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
//@CessConfig
public class GatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

}
