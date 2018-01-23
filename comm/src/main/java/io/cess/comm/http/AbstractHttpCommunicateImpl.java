package io.cess.comm.http;

/**
 * Created by lin on 1/9/16.
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.cess.comm.httpdns.HttpDNS;
import io.cess.util.thread.ActionExecute;
import io.cess.comm.httpdns.HttpDNS;
import io.cess.util.Action;


/**
 *
 * @author 王江林
 * @date 2013-7-29 下午9:13:22
 *
 */
public abstract class AbstractHttpCommunicateImpl implements HttpCommunicateImpl{

    private int mTimeout = 10000;
    private String mName;
    private Map<String,String> mDefaultHeaders = new HashMap<String,String>();
    private HttpDNS mHttpDNS;

    private SessionInfo mSessionInfo;

    private boolean mDebug = false;
    /**
     * 通信 URL
     */
    private URL baseUri = null;

    private static long cacheSize = 200 * 1024 * 1024;

//    private boolean mMainThread = false;

    protected AbstractHttpCommunicateImpl(String name,HttpCommunicate c) {
        if(c == null){
            throw new RuntimeException();
        }
        this.mName = name;
        mSessionInfo = new SessionInfo();
    }

    @Override
    public int getTimeout() {
        return mTimeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    @Override
    public HttpDNS getHttpDNS() {
        return mHttpDNS;
    }

    @Override
    public void setHttpDNS(HttpDNS httpDNS) {
        this.mHttpDNS = httpDNS;
    }

    @Override
    public long getCacheSize() {
        return cacheSize;
    }

    @Override
    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public Map<String,String> defaultHeaders(){
        return this.mDefaultHeaders;
    }
    @Override
    public void addHeader(String name, String value){
        mDefaultHeaders.put(name,value);
    }


    @Override
    public void removeHeader(String name){
        mDefaultHeaders.remove(name);
    }

    @Override
    public String getName(){
        return mName;
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
        return mDebug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.mDebug = debug;
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

    private List<HttpRequestListener> mListeners = new ArrayList<HttpRequestListener>();

    @Override
    public void addHttpRequestListener(HttpRequestListener listener){
        mListeners.add(listener);
    }

    @Override
    public void removeHttpRequestListener(HttpRequestListener listener){
        mListeners.remove(listener);
    }

    private void fireRequestResultListener(HttpPackage pack,Object obj, List<io.cess.comm.http.Error> warning){
        for(HttpRequestListener listener : mListeners){
            if(listener != null){
                listener.requestComplete(this, pack, obj, warning);
            }
        }
    }

    private void fireRequestFaultListener(HttpPackage pack, Error error){
        for(HttpRequestListener listener : mListeners){
            if(listener != null){
                listener.requestFault(this, pack, error);
            }
        }
    }

    private void fireRequestListener(HttpPackage pack){
        for(HttpRequestListener listener : mListeners){
            if(listener != null){
                listener.request(this, pack);
            }
        }
    }


    @Override
    public HttpCommunicateResult<Object> request(final HttpPackage pack){
        return requestImpl(pack,null);
    }

//    protected abstract HttpCommunicateRequest getRequest();

    protected abstract HttpCommunicateHandler getHandler();

    private void processPackHttpHeaders(HttpPackage pack, HttpCommunicate.Params params){
        if (mSessionInfo.cookie != null && mSessionInfo.cookie.length() > 0) {
            params.addHeader("Cookie", mSessionInfo.cookie);
        }
        pack.getRequestHandle().preprocess(pack,params);

        Map<String,String> headers = pack.getHeaders();
        if(headers == null || headers.size() == 0){
            return;
        }
        for(Map.Entry<String,String> item : headers.entrySet()){
            params.addHeader(item.getKey(),item.getValue());
        }
    }


    public <T> HttpCommunicateResult<T> request(HttpPackage<T> pack, ResultFunction<T> result){
        return request(pack,result,null);
    }
    public <T> HttpCommunicateResult<T> request(HttpPackage<T> pack, ResultFunction<T> result, FaultFunction fault){
        return requestImpl(pack, new ResultListener<T>() {
            @Override
            public void result(T obj, List<Error> warning) {
                if(result != null){
                    result.result(obj,warning);
                }
            }

            @Override
            public void fault(Error error) {
                if(fault != null){
                    fault.fault(error);
                }
            }
        });
    }
    @Override
//    	public HttpCommunicateResult request(io.cessclient.http.TcpPackage pack,final ResultFunction result,final FaultFunction fault){
//    public HttpCommunicateResult<Object> request(final io.cess.comm.http.HttpPackage pack, final ResultListener listener, HttpCommunicate.Params params){
    public <T> HttpCommunicateResult<T> request(final HttpPackage<T> pack, final ResultListener<T> listener) {
        return requestImpl(pack,listener);
    }

    private  <T> HttpCommunicateResult<T> requestImpl(final HttpPackage<T> pack, final ResultListener<T> listener){

        this.fireRequestListener(pack);

        HttpCommunicate.Params params = new HttpCommunicate.Params();

        params.setDebug(this.isDebug(pack));
//        params.setMainThread(this.isMainThread(pack));
        params.setTimeout(this.getTimeout(pack));

        processPackHttpHeaders(pack,params);

        final HttpCommunicateResult<T> httpHesult = new HttpCommunicateResult<T>();

//        HttpCommunicateRequest request = this.getRequest();

        final ResultListener listenerImpl = new ResultListener<T>() {

            @Override
            public void result(final T obj, final List<Error> warning) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(true,obj,warning,null);
                        fireResult(httpHesult,listener, obj, warning);
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

            @Override
            public void fault(final Error error) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(false,null,null,error);
                        fireFault(httpHesult,listener,error);
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

//        request.setPackage(pack);
//        request.setImpl(this);
//        request.setListener(listenerImpl);
//        request.setParams(params);

        final HttpCommunicateHandler handler = this.getHandler();

        handler.setPackage(pack);
        handler.setImpl(this);
        handler.setParams(params);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                handler.process(new HttpCommunicateHandler.Listener() {
                    @Override
                    public void response(HttpClientResponse response) {
                        String cookie = response.getHeader("Set-Cookie");

                        if(cookie != null && !"".equals(cookie)) {
                            mSessionInfo.cookie = cookie;
                        }
                        pack.getRequestHandle().response(pack,response,listenerImpl);
                    }
                });
            }
        });

        httpHesult.request = handler;

        return httpHesult;
    }


//    private boolean isMainThread(HttpPackage pack){
//        if(pack.getCommParams() == null || pack.getCommParams().isMainThread() != null){
//            return pack.getCommParams().isMainThread();
//        }
//        return this.mMainThread;
//    }

    protected boolean isDebug(HttpPackage pack){
        if(pack.getCommParams() == null || pack.getCommParams().isDebug() != null){
            return pack.getCommParams().isDebug();
        }
        return this.mDebug;
    }

    protected int getTimeout(HttpPackage pack){
        if(pack.getCommParams() == null || pack.getCommParams().getTimeout() != null){
            return pack.getCommParams().getTimeout();
        }
        return this.mTimeout;
    }
    private void fireResult(final HttpCommunicateResult httpResult,final ResultListener listener,final Object obj,final List<Error> warning){
//        if(listener != null){
//            if(this.isMainThread() && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//                mHandler.post(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        try{
//                            listener.result(obj, warning);
//                        }finally{
//                            httpResult.getAutoResetEvent().set();
////								if(obj instanceof File){
////									((File) obj).delete();
////								}
//                        }
//                    }});
//                return false;
//            }else{
                try{
                    if(listener != null) {
                        listener.result(obj, warning);
                    }
                }finally{
                    httpResult.getAutoResetEvent().set();
                }
//                return true;
//            }
//        }else{
//            httpResult.getAutoResetEvent().set();
//        }
//        return true;
    }

    private void fireFault(final HttpCommunicateResult httpResult,final ResultListener listener,final Error error){
//        if(listener != null){
//            if(this.isMainThread() && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//                mHandler.post(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        try{
//                            listener.fault(error);
//                        }finally{
//                            httpResult.getAutoResetEvent().set();
//                        }
//                    }});
//            }else{
                try{
                    if(listener != null) {
                        listener.fault(error);
                    }
                }finally{
                    httpResult.getAutoResetEvent().set();
                }
//            }
//        }else{
//            httpResult.getAutoResetEvent().set();
//        }
    }
    private void fireProgress(final HttpCommunicateResult httpResult,final ProgressResultListener listener,final long progress,final long total){
        if(listener != null){
//			System.out.println("3 progress:"+progress+"\ttotal:"+total);
//            if(this.isMainThread() && httpResult.threadId != mHandler.getLooper().getThread().getId()){
//                mHandler.post(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        listener.progress(progress,total);
//                    }});
//            }else{
                listener.progress(progress,total);
//            }
        }
    }


    @Override
    public HttpCommunicateResult<FileInfo> download(String file){
        return download(file,null,null);
    }

    @Override
    public HttpCommunicateResult<FileInfo> download(String file, final ResultListener listener){
        return download(file,listener,null);
    }
    @Override
    public HttpCommunicateResult<FileInfo> download(String file, final ResultListener listener, HttpCommunicate.Params params){

        URL url = null;
        try {
            url = new URL(file);
        } catch (MalformedURLException e) {

            //AutoResetEvent set = new AutoResetEvent();
            HttpCommunicateResult result = new HttpCommunicateResult();
            //result.set = set;

            fireFault(result, listener, new Error(-2,null,null,null));

//			result.getAutoResetEvent().set();
            return result;
        }
        if(params == null){
            params = new HttpCommunicate.Params();
            params.setDebug(this.isDebug());
//            params.setMainThread(this.isMainThread());
            params.setTimeout(this.getTimeout());
        }
        return download(url, listener,params);

    }

    @Override
    public HttpCommunicateResult<FileInfo> download(URL file) {
        return download(file,null,null);
    }
    @Override
    public HttpCommunicateResult<FileInfo> download(URL file, final ResultListener listener) {
        return download(file,listener,null);
    }

    protected abstract HttpCommunicateDownloadFile downloadRequest();


    private static ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(10, 50, 15,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());
    @Override
    public HttpCommunicateResult<FileInfo> download(final URL file, final ResultListener listener, HttpCommunicate.Params params){
        if (params == null){
            params = new HttpCommunicate.Params();

            params.setDebug(this.isDebug());
//            params.setMainThread(this.isMainThread());
            params.setTimeout(this.getTimeout());
        }

        ProgressResultListener pListener = null;
        if(listener instanceof ProgressResultListener) {
            pListener = (ProgressResultListener) listener;
        }

        final HttpCommunicateResult<FileInfo> httpHesult = new HttpCommunicateResult<FileInfo>();
//		final AutoResetEvent set = new AutoResetEvent(false);
//		httpHesult.set = set;
        final ProgressResultListener finalPListener = pListener;
        fireRequestListener(null);

        final HttpCommunicateDownloadFile request = this.downloadRequest();

        final ProgressResultListener listenerImpl = new ProgressResultListener<FileInfo>(){
//            private boolean isDeleteFile = false;
            @Override
            public void result(final FileInfo obj,final List<Error> warning) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        popDownload(file.toString());
                    }
                }, new Action() {
                    @Override
                    public void action() {
                        if(obj instanceof FileInfo) {
                            //CacheDownloadFile.save((FileInfo)obj);
                        }
                    }
				}, new Action() {

					@Override
					public void action() {
                        httpHesult.setResult(true,(FileInfo)obj,warning,null);
                        fireResult(httpHesult,listener, obj, warning);
					}
                }, new Action(){

                    @Override
                    public void action() {
                        fireRequestResultListener(null, obj, warning);
                    }

                });
            }

            @Override
            public void fault(final Error error) {
                ActionExecute.execute(new Action() {
                    @Override
                    public void action() {
                        httpHesult.setResult(false,null,null,error);
                        fireFault(httpHesult, listener, error);
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
                if (finalPListener != null){
                    fireProgress(httpHesult, finalPListener,progress,total);
                }
            }
        };//);

//        request.setPackage(pack);


        request.setImpl(this);
        request.setListener(listenerImpl);
        request.setParams(params);

        httpHesult.request = request;

        Runnable task = new Runnable() {
            @Override
            public void run() {
                HttpCommunicateDownloadFile.HttpFileInfo info = request.getFileInfo(file);

                FileInfo cacheFile = null;//CacheDownloadFile.getFileInfo(file.toString());

                if(cacheFile != null && info != null){
                    if(cacheFile.getFile().exists()){
                        if((info.getFileSize() <=0 || cacheFile.getFile().length() == info.getFileSize())
                                && cacheFile.getLastModified() == info.getLastModified()
                                ){

                            listenerImpl.result(cacheFile,null);
                            return;
                        }
                    }
                }

                request.download(file);
            }
        };



//        downloadFile.context = context;
////		downloadFile.http = this.http;
//        downloadFile.download(file);
        pushDownloadTask(file.toString(),task);

        return httpHesult;
    }

    private final static Map<String,Boolean> downloadUrls = new HashMap<String,Boolean>();
    private final static Map<String,Runnable> notDownloadUrls = new HashMap<String,Runnable>();
    private final static Lock lock = new ReentrantLock();

    private void pushDownloadTask(String url,Runnable task){

        lock.lock();
        try {
            if (downloadUrls.containsKey(url)) {
                notDownloadUrls.put(url, task);
            } else {
                downloadUrls.put(url, true);
                mExecutor.execute(task);
            }
        }finally {
            lock.unlock();
        }
    }

    private void popDownload(String url){
        lock.lock();
        try{

            if(notDownloadUrls.containsKey(url)){
                mExecutor.execute(notDownloadUrls.remove(url));
            }
            downloadUrls.remove(url);
        }finally {
            lock.unlock();
        }
    }


//    public boolean isMainThread() {
//        return mMainThread;
//    }
//
//    public void setMainThread(boolean mainThread) {
//        this.mMainThread = mainThread;
//    }


    @Override
    public void newSession() {
        mSessionInfo = new SessionInfo();
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