package io.cess.shorturl;

import io.cess.cloud.CessRestTemplate;
import io.cess.cloud.ResourceRestTemplate;
import io.cess.cloud.config.resourceServer.ResourceServerConfig;
import io.cess.core.CessConfig;
import io.cess.shorturl.config.DataConfig;
import io.cess.shorturl.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

//@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
@ResourceServerConfig
@CessConfig
@Import({DataConfig.class, SwaggerConfig.class})
public class ShortUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlApplication.class, args);
    }

//    @Bean
//    @LoadBalanced
//    RestTemplate restTemplate() {
//        return new CessRestTemplate();
//    }
}
