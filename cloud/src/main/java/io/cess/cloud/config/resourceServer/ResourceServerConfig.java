package io.cess.cloud.config.resourceServer;

import io.cess.cloud.config.TokenConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ResourceServerConfiguration.class,
        MethodSecurityConfig.class,
        TokenConfig.class})
public @interface ResourceServerConfig {
}
