package io.cess.comm.http;


import java.util.List;

/**
 * 
 * @author lin
 * @date 2014年5月15日 下午9:13:14
 *
 */
@FunctionalInterface
public interface ResultFunction<T> {

	public void result(T obj, List<Error> warning);
}
