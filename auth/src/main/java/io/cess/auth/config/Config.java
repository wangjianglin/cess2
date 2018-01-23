package io.cess.auth.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DataConfig.class,UserConfig.class,AuthWebMvcConfig.class})
public @interface Config {
}
