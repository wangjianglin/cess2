package io.cess.auth.config;


import io.cess.cloud.oauth2.AuthorityDecisionService;
import io.cess.cloud.oauth2.OAuth2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 */
@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer {

    /**
     *
     * @param registry
     *
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/oauth/login").setViewName("login");
//        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("index");
    }

//    @Bean
//    public AuthorityDecisionService accessTokenConverter(){
//        return new CessAuthorityDecisionService();
//    }

    @Bean
    public OAuth2Service oAuth2Service(){
        return new AuthService();
    }
}