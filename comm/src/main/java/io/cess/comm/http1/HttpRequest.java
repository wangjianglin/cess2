package io.cess.comm.http1;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.cess.util.Utils;
import io.cess.comm.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:00
 *
 */
public class HttpRequest {

	private io.cess.comm.http1.HttpPackage pack;
	private ResultListener listener;
	//private HttpCommunicateResult result;
	private HttpCommunicateImpl impl;
	/**
	 * json数据的参数名
	 */
//	public static final String JSON_PARAM = "__jsonParam__";
	/**
	 * 客户端请求数据的编码参数方式
	 */
//	public static final String REQUEST_CODING = "__request_coding__";
	
	/**
	 * uri的属性名
	 */
//	public static final String URI = "__uri__";
	
	//public static final String VERSION = "__version__";
//	public static final String VERSION = "0.1";
//	private static final String HTTP_COMM_PROTOCOL = "__http_comm_protocol__";
	public HttpRequest(HttpCommunicateImpl impl, io.cess.comm.http1.HttpPackage pack, ResultListener listener, HttpCommunicateResult result, HttpClient http){
		this.impl = impl;
		this.pack = pack;
		this.listener = listener;
		this.http = http;
		//this.result = result;
	}
	private HttpPost post = null;
	void abort(){
		//result.lock.lock();
		try{
			if(post != null){
				post.abort();
			}
		}
		finally{
			//result.lock.unlock();
		}
	}
	
	
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 10,
			TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	ContentType contentType= ContentType.create("text/plain", "UTF-8");
	private HttpClient http;
	public void request(){
		Runnable task = new Runnable() {

			@Override
			public void run() {
					HttpResponse response;
					ByteArrayOutputStream _out = new ByteArrayOutputStream();
					try {
						//HTTP请求
						post = new HttpPost(HttpUtils.uri(impl,pack));
						post.addHeader(Constants.HTTP_COMM_PROTOCOL, "");
						if(impl.isDebug()){
							post.addHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG, "");
						}
//						ContentBody f = new FileBody(null);
						Map<String,Object> postParams = pack.getRequestHandle().getParams(post,pack);
						if(postParams != null){
							if(pack.isMultipart()){
	//							MultipartRequestEntity d;
								
								 // 初始化客户端请求
	//					        this.initHttpClient();
	//					        Iterator iterator = datas.entrySet().iterator();
	//					        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
	//					        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	//					        multipartEntityBuilder.setCharset(Charset.forName(this.charset));
						       
						        // 生成 HTTP 实体
	//					        HttpEntity httpEntity = multipartEntityBuilder.build();"plan/text",Charset.forName("utf-8"));
	//							Charset c = Charset.forName("utf-8");
	//							ContentType type = null;//new ContentType("", c, null);
								 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
								    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
								    builder.setCharset(Charset.forName("UTF-8"));
	//							    MultipartEntity m = new MultipartEntity();
								    if(postParams != null && postParams.size()>0){
										for(String key : postParams.keySet()){
											if(postParams.get(key) instanceof String){
//												builder.addTextBody(key, (String) postParams.get(key));
												builder.addPart(key, new StringBody((String) postParams.get(key),contentType));
	//											m.addPart(key, new StringBody((String) postParams.get(key)));
											}else{
	//											m.addPart(key, (ContentBody) postParams.get(key));
												builder.addPart(key, (ContentBody) postParams.get(key));
											}
										}
									}
	//							    post.setEntity(m);
								    post.setEntity(builder.build());
							}else{
								List<NameValuePair> params = new 
										ArrayList<NameValuePair>();  
								if(postParams != null && postParams.size()>0){
									for(String key : postParams.keySet()){
										params.add(new BasicNameValuePair(key,(String)postParams.get(key)));
									}
								}
								try {
									post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(params,"utf-8"));
								} catch (UnsupportedEncodingException e1) {
									e1.printStackTrace();
								}
							}
						}
//						http.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
//						http.getParams().setContentCharset("GB2312");
//						http.getParams().setParameter(name, value)
						
						
//						http.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
//						//读取超时
//						http.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
//						链接超时
//						http.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
//						http.getConnectionManager().
//						post.
////						读取超时
//						http.getHttpConnectionManager().getParams().setSoTimeout(60000)
						
						RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();//设置请求和传输超时时间
						post.setConfig(requestConfig);
						
						response = http.execute(post);
						HttpEntity entity = response.getEntity();
						InputStream _in = entity.getContent();
						byte bs[] = new byte[4096];
						int count = 0;
						while((count = _in.read(bs)) != -1){
							_out.write(bs, 0, count);
						}
						_in.close();
					} catch (Throwable e) {
						Error error = new Error();
						error.setCode(-2);
//						error.setCause(e.getCause().getMessage());
						error.setMessage(Utils.printStackTrace(e));
//						error.setStackTrace(e.pr);
						
						HttpUtils.fireFault(listener::fault, error);
						return;
					}
					pack.getRequestHandle().response(pack, _out.toByteArray(), listener);
			}
		};
		executor.execute(task);
	}
	
//	private CloseableHttpClient http;// = HttpClients.createDefault();
//	public void request(){
//		Thread thread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				//result.lock.lock();
//					HttpResponse response;
//					String jsonParam = null;
//					long errorCode = 0;
//					try {
//						//HTTP请求
//						post = new HttpPost(HttpUtils.uri(impl,pack));
//						if(impl.isDebue()){
//							post.addHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG, "");
//						}
//						//post.addHeader(Constants.HTTP_COMM_PROTOCOL, Constants.HTTP_VERSION);
//						//http.setCookieSpecs(new CookieSpecRegistry());
//						Map<String,String> postParams = pack.getRequestHandle().getParams(post,pack);
//						List<NameValuePair> params = new 
//								ArrayList<NameValuePair>();  
//		
//								                //params.add(new BasicNameValuePair
//						if(postParams != null && postParams.size()>0){
//							for(String key : postParams.keySet()){
//								params.add(new BasicNameValuePair(key,postParams.get(key)));
//							}
//						}
//						try {
//							post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(params,"utf-8"));
//						} catch (UnsupportedEncodingException e1) {
//							e1.printStackTrace();
//						}
//					//entity = new 
//						response = http.execute(post);
//						HttpEntity entity = response.getEntity();
//						StringBuffer buffer = new StringBuffer();
//						InputStream _in = entity.getContent();
//						byte bs[] = new byte[4096];
//						int count = 0;
//						while((count = _in.read(bs)) != -1){
//							buffer.append(new String(bs,0,count));
//						}
//						jsonParam = buffer.toString();
////						jsonParam = URLDecoder.decode(jsonParam, "utf-8");
////						//jsonParam = jsonParam;
////						//byte[] tmpBs = new BASE64Decoder().decodeBuffer(jsonParam);
////						byte[] tmpBs = Base64.getDecoder().decode(jsonParam);
////						jsonParam = new String(tmpBs,"utf-8");
//						//System.out.println("json:"+tmpJsonParams);
//						//jsonParam = new String(jsonParam.getBytes(),System.getProperty("sun.jnu.encoding"));
//						
//						//System.out.println("data:"+buffer.toString());
//						//System.out.println("result:"+jsonParam);
//					} catch (Throwable e) {
//						//e.printStackTrace();
//						Error error = new Error();
//						error.setCode(errorCode);
//						HttpUtils.fireFault(listener::fault, error);
//						return;
//					}
//						pack.getRequestHandle().response(pack, jsonParam, listener);
//			}
//		});
//		thread.setDaemon(true);
//		thread.start();
//	}
}
