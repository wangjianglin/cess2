package io.cess.comm.http1;

import java.io.File;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:48
 * 
 */
public class HttpCommunicate {

	private HttpCommunicate(){}
	private static HttpCommunicate _tmp = new HttpCommunicate();
//	public static final HttpCommunicate global = new HttpCommunicateImpl("global");
//	
//	
//}
	//用soft引用实现
	private static List<HttpRequestListener> listeners = new ArrayList<HttpRequestListener>();
	
	
	public static boolean isDebug() {
		return global.isDebue();
	}

	public static void setDebug(boolean debug) {
		global.setDebue(debug);
	}
	
	private static HttpRequestListener globalListner =new HttpRequestListener(){

		@Override
		public void HttpRequest(HttpCommunicateType type, Object obj) {
			for(HttpRequestListener listener : listeners){
				listener.HttpRequest(type, obj);
			}
		}

		@Override
		public void HttpRequestComplete(HttpCommunicateType type, Object obj1,
				Object obj2, List<Error> warning) {
			for(HttpRequestListener listener : listeners){
				listener.HttpRequestComplete(type, obj1,obj2,warning);
			}
		}

		@Override
		public void HttpRequestFault(HttpCommunicateType type, Object obj,
				Error error) {
			for(HttpRequestListener listener : listeners){
				listener.HttpRequestFault(type, obj,error);
			}
		}};

		public void newSession(){
			global.newSession();
		}
	//private static Map<String, HttpCommunicateImpl> impls = new HashMap<String, HttpCommunicateImpl>();
	//	private static Map<String,WeakReference<HttpCommunicateImpl>> impls = new HashMap<String, WeakReference<HttpCommunicateImpl>>();
		private static Map<String,SoftReference<HttpCommunicateImpl>> impls = new HashMap<String, SoftReference<HttpCommunicateImpl>>();

	public static HttpCommunicateImpl get(String name) {
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
				HttpCommunicateImpl himpl = new HttpCommunicateImpl(name,_tmp);
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

	private final static HttpCommunicateImpl global = get("Global");

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
		global.setCommUrl(url);
	}

	public static URL getCommUrl() {
		return global.getCommUrl();
	}

	public static boolean isDebue() {
		return global.isDebue();
	}

	public static void setDebue(boolean debue) {
		global.setDebue(debue);
	}
	/**
	 * 设置代理
	 * 
	 * @param proxy
	 */
//	public static void setAuthenticationHandler(AuthenticationHandler proxy) {
//		global.setAuthenticationHandler(proxy);
//	}


	/**
	 * 
	 * @param credsProvider
	 */
//	public static void setCredentialsProvider(CredentialsProvider credsProvider) {
//		global.setCredentialsProvider(credsProvider);
//	}

	public static void addHttpRequestListener(HttpRequestListener listener) {
		global.addHttpRequestListener(listener);
	}

	public static void addGlobalHttpRequestListener(HttpRequestListener listener) {
		listeners.add(listener);
	}

	public static void removeHttpRequestListener(HttpRequestListener listener) {
		global.removeHttpRequestListener(listener);
	}

	public static void removeGlobaHttpRequestListener(
			HttpRequestListener listener) {
		listeners.remove(listener);
	}

	// private static CookieStore cookieStore = new BasicCookieStore();
	static CookieStore getCookieStore() {
		return global.getCookieStore();
	}

//	public static HttpCommunicateResult request(
//			io.cessclient.http.packages.TcpPackage pack, final ResultListener listener) {
//		return global.request(pack, listener);
//	}
	
	public static HttpCommunicateResult request(io.cess.comm.http1.HttpPackage pack, ResultListener listener){
		if(listener != null){
			return global.request(pack,listener::result,listener::fault);
		}
		return global.request(pack,null,null);
	}
	
	public static HttpCommunicateResult request(io.cess.comm.http1.HttpPackage pack, ResultFunction result){
		return global.request(pack,result,null);
	}
	
	public static HttpCommunicateResult request(io.cess.comm.http1.HttpPackage pack, final ResultFunction result, final FaultFunction fault){
		return global.request(pack,result,fault);
	}

	public static HttpCommunicateResult upload(File file,
			ResultListener listener) {
		return global.upload(file, listener);
	}

	public static HttpCommunicateResult download(String file,
			ResultListener listener) {
		return global.download(file, listener);
	}

	public static HttpCommunicateResult download(URL file,
			ResultListener listener) {
		return global.download(file, listener);
	}
}
