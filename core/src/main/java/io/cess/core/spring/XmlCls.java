package io.cess.core.spring;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XmlCls {

    Class<?>[] cls();
}
