package io.cess.web;


import io.cess.comm.http.AbstractHttpRequestHandle;
import io.cess.comm.http.HttpClientResponse;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.ResultListener;

/**
 * 
 * @author lin
 * @date Mar 8, 2015 3:01:41 PM
 *
 *
 */
public class ProxyHttpRequestHandle extends AbstractHttpRequestHandle {


	@Override
	public void response(HttpPackage pack, HttpClientResponse response, ResultListener listener) {
		//HttpUtils.fireResult(listener::result,response.getData(),null);
	}


//	@Override
//	public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
//		// TODO Auto-generated method stub
//		
//	}

}
