package io.cess.gateway;

import io.cess.gateway.config.CessEnableZuulProxy;
import org.springframework.cloud.client.SpringCloudApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.WebApplicationType;

@CessEnableZuulProxy
@SpringCloudApplication
public class GatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

}
