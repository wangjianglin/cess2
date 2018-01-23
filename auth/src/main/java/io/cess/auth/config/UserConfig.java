package io.cess.auth.config;

import io.cess.cloud.oauth2.OAuth2ClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Bean
    public CustomLoginUserDetailsService loginUserDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user").password("{noop}password").authorities("FOO_READ").build());
//        manager.createUser(User.withUsername("writer").password("{noop}writer").authorities("FOO_READ", "FOO_WRITE").build());
//        return manager;
        return new CustomLoginUserDetailsService();
    }

    @Bean
    public CustomRefreshUserDetailsService refreshUserDetailsService(){
        return new CustomRefreshUserDetailsService();
    }

    @Bean(name = "authClientDetailsService")
    public OAuth2ClientDetailsService clientDetailsService() throws Exception {

        return new AuthClientDetailsService();
//        InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder = new InMemoryClientDetailsServiceBuilder();
//
//        clientDetailsServiceBuilder.withClient("web_app")
//                .secret("{noop}123456")
//                .scopes("FOO","FOO2")
//                .autoApprove(true)
//                .authorities("FOO_READ", "FOO_WRITE")
//                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code","client_credentials")
//                .and()
//                .withClient("demo")
//                .secret("{noop}demo")
//                .scopes("read","write")
//                .autoApprove(true)
//                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code","client_credentials");
//
//        return clientDetailsServiceBuilder.build();
    }
}
