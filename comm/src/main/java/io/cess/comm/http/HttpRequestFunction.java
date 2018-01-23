package io.cess.comm.http;

import io.cess.comm.http1.HttpCommunicateType;

/**
 * 
 * @author lin
 * @date 2014年5月15日 下午9:09:44
 *
 */
@FunctionalInterface
public interface HttpRequestFunction {

	public void HttpRequest(HttpCommunicateType type, Object obj);
}
