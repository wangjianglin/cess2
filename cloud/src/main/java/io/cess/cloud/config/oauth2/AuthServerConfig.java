package io.cess.cloud.config.oauth2;

import io.cess.cloud.config.TokenConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({AuthorizationServerConfigurer.class,
        AuthWebSecurityConfig.class,
        TokenConfig.class,
        AuthWebMvcConfig.class,
        TokenConfig.class})
public @interface AuthServerConfig {

}
