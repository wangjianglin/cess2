package io.cess.auth;


import io.cess.auth.config.AuthWebMvcConfig;
import io.cess.auth.config.Config;
import io.cess.auth.config.UserConfig;
import io.cess.cloud.config.oauth2.AuthServerConfig;
import io.cess.cloud.config.resourceServer.ResourceServerConfig;
import io.cess.core.CessConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@AuthServerConfig
@ResourceServerConfig
@CessConfig
@Config
@MapperScan("io.cess.auth.mapper")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}