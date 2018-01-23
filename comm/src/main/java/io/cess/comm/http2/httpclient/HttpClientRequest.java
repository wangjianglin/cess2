package io.cess.comm.http2.httpclient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.cess.comm.http2.HttpCommunicate;
import io.cess.comm.http2.HttpCommunicateImpl;
import io.cess.comm.http2.HttpCommunicateRequest;
import io.cess.comm.http2.HttpPackage;
import io.cess.comm.http2.ResultListener;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:00
 *
 */
public class HttpClientRequest implements HttpCommunicateRequest {

	private io.cess.comm.http2.HttpPackage pack;
	private ResultListener listener;
	private HttpCommunicateImpl impl;

	HttpClientRequest(HttpClient http){
		this.http = http;
	}
	
//	public HttpClientRequest(HttpCommunicateImpl impl,io.cessclient.http.HttpPackage pack,ResultListener listener, HttpCommunicateResult result,HttpClient http){
//		this.impl = impl;
//		this.pack = pack;
//		this.listener = listener;
//		this.http = http;
//	}
	
	private HttpPost post = null;
	public void abort(){
		if(post != null){
			post.abort();
		}
	}
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10,
			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	private HttpClient http;

	@Override
	public void setPackage(HttpPackage pack) {
		this.pack = pack;
	}

	@Override
	public void setImpl(HttpCommunicateImpl impl) {
		this.impl = impl;
	}

	@Override
	public void setListener(ResultListener listener) {
		this.listener = listener;
	}

	public void request(){
		Runnable task = new HttpClientRequestRunnable(http,impl,pack,listener);
		executor.execute(task);
//		new Thread(task).start();
	}

	@Override
	public void setParams(HttpCommunicate.Params params) {

	}
}
