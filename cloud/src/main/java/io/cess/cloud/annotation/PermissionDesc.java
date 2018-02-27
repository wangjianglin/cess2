package io.cess.cloud.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(PermissionDescs.class)
public @interface PermissionDesc {
    String code();
    String desc();
    String scope();
    String role();
}
