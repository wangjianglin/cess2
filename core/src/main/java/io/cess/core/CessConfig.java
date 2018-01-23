package io.cess.core;

import io.cess.core.cxf.CxfConfig;
import io.cess.core.web.WebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.*;

/**
 * Created by lin on 28/10/2016.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({WebConfig.class,CxfConfig.class})
public @interface CessConfig {
}
