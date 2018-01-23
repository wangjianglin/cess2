package io.cess.comm.http2;

/**
 * Created by lin on 1/9/16.
 */


import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cess.util.thread.ActionExecute;
import io.cess.util.Action;


/**
 *
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public abstract class AbstractHttpCommunicateImpl implements HttpCommunicateImpl{

    private int timeout = 10000;
    private String name;
    private Map<String,String> defaultHeaders = new HashMap<String,String>();

    private boolean debug = false;
    /**
     * 通信 URL
     */
    private URL baseUri = null;

    private HttpCommunicate.Params defaultParams = new HttpCommunicate.Params();


    //HttpCommunicateImpl(){}
    protected AbstractHttpCommunicateImpl(String name,HttpCommunicate c) {
        if(c == null){
            throw new RuntimeException();
        }
        this.name = name;
        //创建HttpClientBuilder
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        //HttpClient
//        http = httpClientBuilder.build();
//        CookieStore cookie = new BasicCookieStore();
//        http = HttpClients.custom().useSystemProperties()
//                .setDefaultCookieStore(cookie)
//                .build();
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    //	public HttpCommunicateImpl(){
//	}



    public Map<String,String> defaultHeaders(){
        return this.defaultHeaders;
    }
    @Override
    public void addHeader(String name, String value){
        defaultHeaders.put(name,value);
    }


    @Override
    public void removeHeader(String name){
        defaultHeaders.remove(name);
    }

    @Override
    public String getName(){
        return name;
    }


    /**
     * 设置通信 URL
     * @param url
     */

    @Override
    public void setCommUrl(URL url){
        String uriString = url.toString();
        if(uriString.endsWith("/")){
            try {
                baseUri = new URL(uriString.substring(0, uriString.length() - 1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                baseUri = new URL(uriString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public URL getCommUrl(){
        return baseUri;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
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

    private List<SoftReference<HttpRequestListener>> listeners = new ArrayList<SoftReference<HttpRequestListener>>();

    @Override
    public void addHttpRequestListener(HttpRequestListener listener){
        SoftReference<HttpRequestListener> slistener = new SoftReference<HttpRequestListener>(listener);
        listeners.add(slistener);
    }

    @Override
    public void removeHttpRequestListener(HttpRequestListener listener){
        for(SoftReference<HttpRequestListener> item : listeners){
            if(item.get() == listener){
                listeners.remove(item);
                break;
            }
        }
    }

    private void fireRequestResultListener(HttpPackage pack,Object obj, List<io.cess.comm.http2.Error> warning){
        for(SoftReference<HttpRequestListener> item : listeners){
            if(item.get() != null){
                item.get().requestComplete(this, pack, obj, warning);
            }
        }
    }

    private void fireRequestFaultListener(HttpPackage pack, Error error){
        for(SoftReference<HttpRequestListener> item : listeners){
            if(item.get() != null){
                item.get().requestFault(this, pack, error);
            }
        }
    }

    private void fireRequestListener(HttpPackage pack){
        for(SoftReference<HttpRequestListener> item : listeners){
            if(item.get() != null){
                item.get().request(this, pack);
            }
        }
    }

//    private CookieStore cookieStore = new BasicCookieStore();
//    CookieStore getCookieStore(){
//        return cookieStore;
//    }


//    @Override
//    public void newSession(){
//        cookieStore = new BasicCookieStore();
//    }

//	public HttpCommunicateResult request(io.cessclient.http.TcpPackage pack,ResultListener listener){
//		if(listener != null){
//			return request(pack,listener::result,listener::fault);
//		}
//		return request(pack,null,null);
//	}

//	public HttpCommunicateResult request(io.cessclient.http.TcpPackage pack,ResultFunction result){
//		return request(pack,result,null);
//	}

//	private CloseableHttpClient http;// = HttpClients.createDefault();
//	private DefaultHttpClient http = new DefaultHttpClient();

    //Java调用
//	loseableHttpClient httpclient = HttpClients.custom()
//            .setDefaultCookieStore(cookies)
//            .build();
//Android修改为

//    private CloseableHttpClient http;// = HttpClients.custom().useSystemProperties()
//            .setDefaultCookieStore(cookies)
//            .build();


    //	private AndroidHTTPClient c = AndroidHttpClient.newInstance("");


    @Override
    public HttpCommunicateResult request(final HttpPackage pack, final ResultListener listener){
        return request(pack,listener,defaultParams);
    }

    protected abstract HttpCommunicateRequest getRequest();

    @Override
    public HttpCommunicateResult request(final HttpPackage pack, final ResultListener listener, HttpCommunicate.Params params) {
        return request(pack,listener::result,listener::fault,params);
    }

    @Override
    public HttpCommunicateResult request(HttpPackage pack, final ResultFunction result) {
        return request(pack,result,null,defaultParams);
    }

    @Override
    public HttpCommunicateResult request(HttpPackage pack, final ResultFunction result, final FaultFunction fault) {
        return request(pack,result,fault,defaultParams);
    }

    @Override
    public HttpCommunicateResult request(HttpPackage pack, final ResultFunction result, final FaultFunction fault, HttpCommunicate.Params params){
//    public HttpCommunicateResult request(final io.cessclient.http.HttpPackage pack, final ResultListener listener, HttpCommunicate.Params params){

        if (params == null){
            params = defaultParams;
        }

        this.fireRequestListener(pack);

        final HttpCommunicateResult httpHesult = new HttpCommunicateResult();

        HttpCommunicateRequest request = this.getRequest();

        //		final AutoResetEvent set = new AutoResetEvent(false);
//        HttpURLConnectionRequest request = new HttpURLConnectionRequest(this,pack, new ResultListener() {
        ResultListener listenerImpl = new ResultListener() {

            @Override
            public void result(final Object obj, final List<Error> warning) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(true,obj);
                        fireResult(httpHesult,result, obj, warning);
                    }
                    //					}, new Action() {
                    //
                    //						@Override
                    //						public void action() {
                    //
                    //							set.set();
                    //						}
                }, new Action(){

                    @Override
                    public void action() {
                        fireRequestResultListener(pack, obj, warning);
                    }

                });
            }

            //			@Override
            //			public void progress(long count, long total) {
            //				//HttpUtils.fireProgress(ProgressFunction,count,total);
            //			}

            @Override
            public void fault(final Error error) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(false,null);
                        fireFault(httpHesult,fault,error);
                    }
                    //				}, new Action() {
                    //
                    //					@Override
                    //					public void action() {
                    //						set.set();
                    //					}
                }, new Action(){

                    @Override
                    public void action() {
                        fireRequestFaultListener(pack, error);
                    }});
            }
        };//);

        request.setPackage(pack);
        request.setImpl(this);
        request.setListener(listenerImpl);
        request.setParams(params);

        httpHesult.request = request;
        //		httpHesult.set = set;
        request.request();
        return httpHesult;
    }

//    public HttpCommunicateResult request2(final io.cessclient.http.HttpPackage pack,final ResultListener listener){
//
//        this.fireRequestListener(pack);
//
//        final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
////		final AutoResetEvent set = new AutoResetEvent(false);
//        HttpRequest request = new HttpRequest(this,pack, new ResultListener() {
//
//            @Override
//            public void result(final Object obj, final List<Error> warning) {
//                ActionExecute.execute(new Action() {
//                    @Override
//                    public void action() {
//                        httpHesult.setResult(true,obj);
//                        fireResult(httpHesult,listener, obj, warning);
//                    }
////					}, new Action() {
////
////						@Override
////						public void action() {
////
////							set.set();
////						}
//                },new Action(){
//
//                    @Override
//                    public void action() {
//                        fireRequestResultListener(pack, obj, warning);
//                    }
//
//                });
//            }
//
////			@Override
////			public void progress(long count, long total) {
////				//HttpUtils.fireProgress(ProgressFunction,count,total);
////			}
//
//            @Override
//            public void fault(final Error error) {
//                ActionExecute.execute(new Action() {
//                    @Override
//                    public void action() {
//                        httpHesult.setResult(false,null);
//                        fireFault(httpHesult,listener,error);
//                    }
////				}, new Action() {
////
////					@Override
////					public void action() {
////						set.set();
////					}
//                },new Action(){
//
//                    @Override
//                    public void action() {
//                        fireRequestFaultListener(pack, error);
//                    }});
//            }
//        },httpHesult,http);
//        httpHesult.request = request;
////		httpHesult.set = set;
//        request.request();
//        return httpHesult;
//    }

    private void fireResult(final HttpCommunicateResult httpResult,ResultFunction result,final Object obj,final List<Error> warning){

        try{
            if(result != null) {
                result.result(obj, warning);
            }
        }finally{
            httpResult.getAutoResetEvent().set();
        }
    }
    //	private void fireProgress(HttpCommunicateResult httpHesult,ResultListener listener,long count,long total){
//			if(listener != null){
//				listener.progress(count, total);
//			}
//		}
    private void fireFault(final HttpCommunicateResult httpResult, FaultFunction fault, Error error){

        try{
            if(fault != null) {
                fault.fault(error);
            }
        }finally{
            httpResult.getAutoResetEvent().set();
        }
    }
    private void fireProgress(final HttpCommunicateResult httpResult,ProgressFunction progressFun,final long progress,final long total){
        if(progressFun != null){
//			System.out.println("3 progress:"+progress+"\ttotal:"+total);
//            if(this.mainThread && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//                mHandler.post(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        listener.progress(progress,total);
//                    }});
//            }else{
            progressFun.progress(progress,total);
//            }
        }
    }

//	public HttpCommunicateResult upload(File file,ResultListener listener){
//		return null;
//	}

    @Override
    public HttpCommunicateResult download(String file, final ResultListener listener){
        return download(file,listener,defaultParams);
    }

    @Override
    public HttpCommunicateResult download(String file, final ResultListener listener, HttpCommunicate.Params params){
        if (listener instanceof ProgressResultListener){
            ProgressResultListener pListener = (ProgressResultListener) listener;
            return download(file,pListener::result,pListener::fault,pListener::progress,params);
        }
        return download(file,listener::result,listener::fault,null,params);
    }

    @Override
    public HttpCommunicateResult download(String file, ResultFunction result,FaultFunction fault,ProgressFunction progress, HttpCommunicate.Params params){
        URL url = null;
        try {
            url = new URL(file);
        } catch (MalformedURLException e) {

            //AutoResetEvent set = new AutoResetEvent();
            HttpCommunicateResult httpResult = new HttpCommunicateResult();
            //result.set = set;

            fireFault(httpResult, fault, new Error(-2,null,null,null));

//			result.getAutoResetEvent().set();
            return httpResult;
        }
        return download(url, result,fault,progress,params);
    }

    @Override
    public HttpCommunicateResult download(String file, ResultFunction result,FaultFunction fault,ProgressFunction progress){
        return download(file,result,fault,progress,defaultParams);
    }

    @Override
    public HttpCommunicateResult download(URL file, final ResultListener listener) {
        return download(file,listener,defaultParams);
    }

    protected abstract HttpCommunicateDownloadFile downloadRequest();

    @Override
    public HttpCommunicateResult download(URL file, ResultListener listener, HttpCommunicate.Params params) {
        if (listener instanceof ProgressResultListener){
            ProgressResultListener pListener = (ProgressResultListener) listener;
            return download(file,pListener::result,pListener::fault,pListener::progress,params);
        }
        return download(file,listener::result,listener::fault,null,params);
    }

    @Override
    public HttpCommunicateResult download(URL file, ResultFunction result,FaultFunction fault,ProgressFunction progress){
        return download(file,result,fault,progress,defaultParams);
    }

    @Override
    public HttpCommunicateResult download(URL file, ResultFunction result,FaultFunction fault,ProgressFunction progressFun, HttpCommunicate.Params params){
        if (params == null){
            params = defaultParams;
        }

//        ProgressResultListener pListener = null;
//        if(listener instanceof ProgressResultListener) {
//            pListener = (ProgressResultListener) listener;
//        }

        final HttpCommunicateResult httpHesult = new HttpCommunicateResult();
//		final AutoResetEvent set = new AutoResetEvent(false);
//		httpHesult.set = set;
//        final ProgressResultListener finalPListener = pListener;
        fireRequestListener(null);

        HttpCommunicateDownloadFile request = this.downloadRequest();

        ProgressResultListener listenerImpl = new ProgressResultListener(){
//            private boolean isDeleteFile = false;
            @Override
            public void result(final Object obj,final List<Error> warning) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(true,obj);
                        fireResult(httpHesult,result, obj, warning);
                    }
//				}, new Action() {//fireResult中已经set了

//					@Override
//					public void action() {
//
//						httpHesult.getAutoResetEvent().set();
//					}
                }, new Action(){

                    @Override
                    public void action() {
                        try {
                            fireRequestResultListener(null, obj, warning);
                        }finally {
//                            if(isDeleteFile && obj instanceof File){
//                                ((File)obj).delete();
//                            }
                        }

                    }

                });
            }

            @Override
            public void fault(final Error error) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(false,null);
                        fireFault(httpHesult, fault, error);
                    }
//				}, new Action() {//fireFault中已经set了
//
//					@Override
//					public void action() {
//						httpHesult.getAutoResetEvent().set();
//					}
                }, new Action(){

                    @Override
                    public void action() {
                        fireRequestFaultListener(null, error);
                    }});
            }

            @Override
            public void progress(long progress, long total) {
//				System.out.println("2 progress:"+progress+"\ttotal:"+total);
                if (progressFun != null){
                    fireProgress(httpHesult, progressFun,progress,total);
                }
            }
        };//);

//        request.setPackage(pack);
        request.setImpl(this);
        request.setListener(listenerImpl);
        request.setParams(params);

        httpHesult.request = request;

        request.download(file);
//        downloadFile.context = context;
////		downloadFile.http = this.http;
//        downloadFile.download(file);

        return httpHesult;
    }


//    protected Error error(long code,String message,String cause,String stackTrace){
//        Error error = new Error();
//        error.setCode(code);
//        error.setMessage(message);
//        error.setStackTrace(stackTrace);
//        error.setCause(cause);
//
//        return error;
//    }
}