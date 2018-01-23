package io.cess.cloud.config.oauth2;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 */
@Configuration
@Order(-30)
public class AuthWebMvcConfig implements WebMvcConfigurer {

    /**
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/oauth/login").setViewName("login");
    }
}