package io.cess.comm.http;


import java.net.URL;
import java.util.Map;

import io.cess.comm.httpdns.HttpDNS;
import io.cess.comm.httpdns.HttpDNS;

/**
 * Created by lin on 1/9/16.
 */
public interface HttpCommunicateImpl{
//    void init();

    void setHttpDNS(HttpDNS httpDNS);
    HttpDNS getHttpDNS();

    long getCacheSize();

    void setCacheSize(long cacheSize);

    int getTimeout();

    void setTimeout(int timeout);

    void addHeader(String name, String value);

    void removeHeader(String name);

    String getName();

    void setCommUrl(URL url);

    URL getCommUrl();

    boolean isDebug();

    void setDebug(boolean debug);

    void addHttpRequestListener(HttpRequestListener listener);

    void removeHttpRequestListener(HttpRequestListener listener);

    <T> HttpCommunicateResult<T> request(HttpPackage<T> pack, ResultListener<T> listener);
    <T> HttpCommunicateResult<T> request(HttpPackage<T>  pack);

    <T> HttpCommunicateResult<T> request(HttpPackage<T> pack, ResultFunction<T> result);
    <T> HttpCommunicateResult<T> request(HttpPackage<T> pack, ResultFunction<T> result, FaultFunction fault);

    HttpCommunicateResult<FileInfo> download(String file);
    HttpCommunicateResult<FileInfo> download(String file, ResultListener<FileInfo> listener);

    HttpCommunicateResult<FileInfo> download(String file, ResultListener<FileInfo> listener, HttpCommunicate.Params params);

    HttpCommunicateResult<FileInfo> download(URL file);
    HttpCommunicateResult<FileInfo> download(URL file, ResultListener<FileInfo> listener);

    HttpCommunicateResult<FileInfo> download(URL file, ResultListener<FileInfo> listener, HttpCommunicate.Params params);

//    boolean isMainThread();
//
//    void setMainThread(boolean mMainThread);

    void newSession();

    Map<String,String> defaultHeaders();

//    void setType(HttpCommunicateType type);
}
