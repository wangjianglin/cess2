package io.cess.comm.http1;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.cess.util.thread.ActionExecute;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.cess.util.Action;
import io.cess.util.thread.AutoResetEvent;

/**
 * 
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public class HttpCommunicateImpl{// implements HttpCommunicate{

	private String name;
	private boolean debug;

	HttpCommunicateImpl(String name,HttpCommunicate c) {
		if(c == null){
			throw new RuntimeException();
		}
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	private boolean debue = false;
	/**
	 * 通信 URL
	 */
		private URL baseUrl = null;
		
		/**
		 * 设置通信 URL
		 * @param url
		 */
		public void setCommUrl(URL url){
			String uriString = url.toString();
			if(uriString.endsWith("/")){
				try {
					baseUrl = new URL(uriString.substring(0, uriString.length() - 1));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					baseUrl = new URL(uriString);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		public URL getCommUrl(){
			return baseUrl;
		}
		
public boolean isDebue() {
			return debue;
		}

		public void setDebue(boolean debue) {
			this.debue = debue;
		}

		//		/**
//		 * 代理对象
//		 */
//		private AuthenticationHandler authenticationHandler=null;
//		/**
//		 * 设置代理
//		 * @param proxy
//		 */
//		public void setAuthenticationHandler(AuthenticationHandler proxy){
//			authenticationHandler = proxy;
//		}
//		
//		private CredentialsProvider credentialsProvider;
//		
		/**
		 * 
		 * @param credsProvider
		 */
//		public void setCredentialsProvider(CredentialsProvider credsProvider){
//			credentialsProvider = credsProvider;
//		}

	public void addHttpRequestListener(HttpRequestListener listener){
			
		}
	public void removeHttpRequestListener(HttpRequestListener listener){
		
	}

	private CookieStore cookieStore = new BasicCookieStore();
	 CookieStore getCookieStore(){
		return cookieStore;
	}

	 public void newSession(){
		 cookieStore = new BasicCookieStore();
	 }

	public HttpCommunicateResult request(HttpPackage pack, ResultListener listener){
		if(listener != null){
			return request(pack,listener::result,listener::fault);
		}
		return request(pack,null,null);
	}
	
	public HttpCommunicateResult request(HttpPackage pack, ResultFunction result){
		return request(pack,result,null);
	}
	
	private CloseableHttpClient http = HttpClients.createDefault();
	
	public HttpCommunicateResult request(HttpPackage pack, final ResultFunction result, final FaultFunction fault){
		final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
		final AutoResetEvent set = new AutoResetEvent(false);
		HttpRequest request = new HttpRequest(this,pack, new ResultListener() {
			
			@Override
				public void result(final Object obj, final List<Error> warning) {
					ActionExecute.execute(new Action() {
						@Override
						public void action() {
							httpHesult.setResult(true);
							HttpUtils.fireResult(result, obj, warning);
						}
					}, new Action() {

						@Override
						public void action() {

							set.set();
						}
					});
				}
			
			@Override
			public void progress(long count, long total) {
				//HttpUtils.fireProgress(ProgressFunction,count,total);
			}
			
			@Override
			public void fault(final Error error) {
				ActionExecute.execute(new Action() {
					@Override
					public void action() {
						httpHesult.setResult(false);
						HttpUtils.fireFault(fault,error);
					}
				}, new Action() {

					@Override
					public void action() {
						set.set();
					}
				});
			}
		},httpHesult,http);
		httpHesult.request = request;
		httpHesult.set = set;
		request.request();
		return httpHesult;
	}

	public HttpCommunicateResult upload(File file,ResultListener listener){
		return null;
	}

	public HttpCommunicateResult download(String file, ResultListener listener){
		return null;
	}

	public HttpCommunicateResult download(URL file, ResultListener listener){
		return null;
	}
	}