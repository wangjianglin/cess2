package io.cess.util;

/**
 * Created by lin on 1/28/16.
 */

@FunctionalInterface
public interface Function<R,T> {

    R function(T t);
}
