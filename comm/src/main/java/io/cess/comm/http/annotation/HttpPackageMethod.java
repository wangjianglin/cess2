package io.cess.comm.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpMethod;

/**
 * 
 * @author lin
 * @date Mar 8, 2015 10:24:20 PM
 *
 */
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpPackageMethod {

	HttpMethod value();
}
