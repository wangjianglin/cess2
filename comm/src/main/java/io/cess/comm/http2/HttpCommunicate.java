package io.cess.comm.http2;


import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cess.comm.http2.httpclient.HttpClientCommunicateImpl;
import io.cess.comm.http2.httpurlconnection.HttpURLConnectionCommunicateImpl;
import io.cess.comm.http2.httpclient.HttpClientCommunicateImpl;
import io.cess.comm.http2.httpurlconnection.HttpURLConnectionCommunicateImpl;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:48
 * 
 */
public class HttpCommunicate {

	public static class Params{
		private boolean mainThread;
		private boolean debug;

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public boolean isMainThread() {
			return mainThread;
		}

		public void setMainThread(boolean mainThread) {
			this.mainThread = mainThread;
		}
	}


	private static HttpCommunicateType type = HttpCommunicateType.HttpURLConnection;

	public static void setType(HttpCommunicateType type){
		HttpCommunicate.type = type;
	}

	private HttpCommunicate(){}
	private static HttpCommunicate _tmp = new HttpCommunicate();
//	public static final HttpCommunicate global = new HttpCommunicateImpl("global");
//	
//	
//}
	//用soft引用实现
	private static List<SoftReference<HttpRequestListener>> listeners = new ArrayList<SoftReference<HttpRequestListener>>();
	
	private static HttpRequestListener globalListner =new HttpRequestListener(){

		@Override
		public void request(HttpCommunicateImpl impl, HttpPackage pack) {
			HttpRequestListener item = null;
			for(SoftReference<HttpRequestListener> listener : listeners){
				item = listener.get();
				if(item == null){
					continue;
				}
				item.request(impl, pack);
			}
		}

		@Override
		public void requestComplete(HttpCommunicateImpl impl, HttpPackage pack, Object obj, List<Error> warning) {
			HttpRequestListener item = null;
			for(SoftReference<HttpRequestListener> listener : listeners){
				item = listener.get();
				if(item == null){
					continue;
				}
				item.requestComplete(impl, pack,obj,warning);
			}
		}

		@Override
		public void requestFault(HttpCommunicateImpl impl, HttpPackage pack, Error error) {
			HttpRequestListener item = null;
			for(SoftReference<HttpRequestListener> listener : listeners){
				item = listener.get();
				if(item == null){
					continue;
				}
				item.requestFault(impl, pack,error);
			}
		}

	};

		public void newSession(){
			global().newSession();
		}
	//private static Map<String, HttpCommunicateImpl> impls = new HashMap<String, HttpCommunicateImpl>();
	//	private static Map<String,WeakReference<HttpCommunicateImpl>> impls = new HashMap<String, WeakReference<HttpCommunicateImpl>>();
		private static Map<String,SoftReference<HttpCommunicateImpl>> impls = new HashMap<String, SoftReference<HttpCommunicateImpl>>();

	public static HttpCommunicateImpl get(String name) {
		return get(name,type);
	}

	public static HttpCommunicateImpl get(String name,HttpCommunicateType type) {
		SoftReference<HttpCommunicateImpl> impl = impls.get(name);
		if (impl != null && impl.get() != null) {
			return impl.get();
		}

		synchronized (impls) {
			impl = impls.get(name);
			if(impl != null && impl.get() == null){
				impls.remove(name);
			}
			if (impl == null || impl.get() == null) {
//				HttpCommunicateImpl himpl = new HttpCommunicateImpl(name,_tmp);
				HttpCommunicateImpl himpl =null;
				if(type == HttpCommunicateType.HttpURLConnection){
					himpl = new HttpURLConnectionCommunicateImpl(name,_tmp);
				}else{
					himpl = new HttpClientCommunicateImpl(name,_tmp);
				}
				impl = new SoftReference<HttpCommunicateImpl>(himpl);
				himpl.addHttpRequestListener(globalListner);

				impls.put(name, impl);
			}
			return impl.get();
		}
	}

	public static void remove(String name) {
		synchronized (impls) {
			SoftReference<HttpCommunicateImpl> impl = impls.remove(name);
			if(impl != null){
				if(impl.get()!=null){
					impl.get().removeHttpRequestListener(globalListner);
				}
			}
			
			ArrayList<String> list = new ArrayList<String>();
			for(String item : impls.keySet()){
				impl = impls.get(item);
				if(impl == null || impl.get() == null){
					list.add(item);
				}
			}
			for(String item : list){
				impls.remove(item);
			}
		}
	}

//	private static HttpCommunicateImpl globalHttpURL = null;//get("Global",HttpCommunicateType.HttpURLConnection);

	private static HttpCommunicateImpl globalImpl = null;

	private static HttpCommunicateImpl global(){
		if(globalImpl != null){
			return globalImpl;
		}
		if(type == HttpCommunicateType.HttpClient){
			globalImpl = get("Global",HttpCommunicateType.HttpClient);
		}else{
			globalImpl = get("Global",HttpCommunicateType.HttpURLConnection);
		}
		return globalImpl;
	}

	/**
	 * 通信 URL
	 */
	// private static URI baseUri = null;

	/**
	 * 设置通信 URL
	 * 
	 * @param url
	 */
	public static void setCommUrl(URL url) {
		global().setCommUrl(url);
	}

	public static URL getCommUrl() {
		return global().getCommUrl();
	}

	public static boolean isDebug() {
		return global().isDebug();
	}

	public static void setDebug(boolean debug) {
		global().setDebug(debug);
	}

	public int getTimeout() {
		return global().getTimeout();
	}

	public void setTimeout(int timeout) {
		global().setTimeout(timeout);
	}
//	/**
//	 * 设置代理
//	 *
//	 * @param proxy
//	 */
//	public static void setAuthenticationHandler(AuthenticationHandler proxy) {
//		global.setAuthenticationHandler(proxy);
//	}


//	/**
//	 *
//	 * @param credsProvider
//	 */
//	public static void setCredentialsProvider(CredentialsProvider credsProvider) {
//		global.setCredentialsProvider(credsProvider);
//	}

	public static void addHttpRequestListener(HttpRequestListener listener) {
		global().addHttpRequestListener(listener);
	}

	public static void addGlobalHttpRequestListener(HttpRequestListener listener) {
		listeners.add(new SoftReference<HttpRequestListener>(listener));
	}

	public static void removeHttpRequestListener(HttpRequestListener listener) {
		for(SoftReference<HttpRequestListener> item : listeners){
			if(item.get() == listener){
				listeners.remove(item);
				return;
			}
		}
	}

	public static void removeGlobaHttpRequestListener(
			HttpRequestListener listener) {
		listeners.remove(listener);
	}

	// private static CookieStore cookieStore = new BasicCookieStore();
//	static CookieStore getCookieStore() {
//		return global.getCookieStore();
//	}

//	public static HttpCommunicateResult request(
//			io.cessclient.http.packages.TcpPackage pack, final ResultListener listener) {
//		return global.request(pack, listener);
//	}

	public static HttpCommunicateResult request(io.cess.comm.http2.HttpPackage pack, ResultListener listener){
//		if(listener != null){
//			return global.request(pack,listener::result,listener::fault);
//		}
		return global().request(pack,listener);
	}

	public static HttpCommunicateResult request(io.cess.comm.http2.HttpPackage pack, ResultListener listener, Params params){
		return global().request(pack,listener,params);
	}
	
	public static HttpCommunicateResult request(io.cess.comm.http2.HttpPackage pack, ResultFunction result){
		return global().request(pack,result,null,null);
	}

	public static HttpCommunicateResult request(io.cess.comm.http2.HttpPackage pack, ResultFunction result, FaultFunction fault){
		return global().request(pack,result,fault);
	}

	public static HttpCommunicateResult request(io.cess.comm.http2.HttpPackage pack, ResultFunction result, FaultFunction fault, Params params){
		return global().request(pack,result,fault,params);
	}

//	public static HttpCommunicateResult upload(File file,
//			ResultListener listener) {
//		return global.upload(file, listener);
//	}


	public static HttpCommunicateResult download(String file,
												 ResultListener listener) {
		return global().download(file,listener);
	}

	public static HttpCommunicateResult download(String file,
												 ResultListener listener,Params params) {
		return global().download(file,listener,params);
	}

	public static HttpCommunicateResult download(URL file,
												 ResultListener listener) {
		return global().download(file, listener);
		//return HttpCommunicateImpl.downloadImpl(file,listener);
	}
	public static HttpCommunicateResult download(URL file,
												  ResultListener listener,Params params) {
		return global().download(file, listener);
		//return HttpCommunicateImpl.downloadImpl(file,listener);
	}

	public static HttpCommunicateResult download(URL file, ResultFunction result,FaultFunction fault,ProgressFunction progress, HttpCommunicate.Params params){
		return global().download(file,result,fault,progress,params);
	}

	public static HttpCommunicateResult download(URL file, ResultFunction result,FaultFunction fault,ProgressFunction progress){
		return global().download(file,result,fault,progress);
	}

	public static HttpCommunicateResult download(String file, ResultFunction result,FaultFunction fault,ProgressFunction progress, HttpCommunicate.Params params){
		return global().download(file,result,fault,progress,params);
	}

	public static HttpCommunicateResult download(String file, ResultFunction result,FaultFunction fault,ProgressFunction progress){
		return global().download(file,result,fault,progress);
	}

}
