package io.cess.comm.http.httpclient;

/**
 * Created by lin on 1/10/16.
 */

import io.cess.comm.http.*;
import io.cess.comm.http.Error;
import io.cess.util.MD5;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

import io.cess.comm.http.FileInfo;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.Error;
import io.cess.comm.http.HttpCommunicateDownloadFile;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.ProgressResultListener;
import io.cess.util.Utils;

/**
 * Created by lin on 9/24/15.
 */

class DownloadFile implements HttpCommunicateDownloadFile {

    private static final int DOWNLOAD_SIZE = 800 * 1024;

    private ProgressResultListener listener;

    private HttpCommunicate.Params params;
    private HttpCommunicateImpl impl;

    private HttpGet get;
    private CookieStore mCookie;

    DownloadFile(){
//        this.mCookie = cookie;
    }


    private HttpClient genHttp(){

        ManagedHttpClientConnectionFactory connFactory = new ManagedHttpClientConnectionFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                String destIp = null;
                if(impl.getHttpDNS() != null){
                    destIp = impl.getHttpDNS().getIpByHost(host);
                }
                if (destIp != null) {
                    return InetAddress.getAllByName(destIp);
                }else {
                    return super.resolve(host);
                }
            }

        };

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(params.getTimeout())
                .setConnectTimeout(params.getTimeout())
                .setConnectionRequestTimeout(params.getTimeout())
                .setStaleConnectionCheckEnabled(true)
                .build();

        return HttpClients.custom().useSystemProperties()
                .setDefaultCookieStore(mCookie)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(new PoolingHttpClientConnectionManager(
                        socketFactoryRegistry,connFactory,dnsResolver))
//				.setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
                .build();
    }


    @Override
    public HttpFileInfo getFileInfo(URL url) {

        try{
            HttpGet get = new HttpGet(url.toString());

            HttpClient http = genHttp();
            HttpResponse response = http.execute(get);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                final HttpEntity entity = response.getEntity();
    //            final long length = entity.getContentLength();
                //                    fileName = response.get
    //            lastModified = 0;

                HttpFileInfo info = new HttpFileInfo();

                info.setLastModified(parserLastModified(response));
                info.setFileSize(entity.getContentLength());
                return info;
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    private long parserLastModified(HttpResponse response){
        Header[] headers = response.getHeaders("Last-Modified");
        if(headers != null && headers.length > 0){
            return new Date(headers[0].getValue()).getTime();
        }
        return 0;
    }

    ////attachment; filename="buyers_own.apk"
    private String parserFileName(HttpResponse response){
        Header[] headers = response.getHeaders("Content-Disposition");
        if(headers != null && headers.length > 0){

            String value = headers[0].getValue();
            if(value != null && value.length() > 23){
                return value.substring(22,value.length()-1);
            }
        }
        return null;
    }

    public void abort(){
        if(get != null){
            get.abort();
        }
    }


    private File file;
    private String fileName;
    private long lastModified;
    private int statusCode;
    private long length;
    private URL url;

    public void download(URL url) {
        this.url = url;
        get = new HttpGet(url.toString());

        boolean isSuccess = false;
        try{
//            isSuccess = runImpl();
            isSuccess = downloadImpl();
        }catch (Throwable e){
            e.printStackTrace();
            Error error = new Error(-3,null,null,Utils.printStackTrace(e));
//                    error.setStackTrace(Utils.printStackTrace(e));
            listener.fault(error);
            return;
        }
        if(isSuccess) {
            listener.result(new FileInfo(get.getURI().toString(),file, fileName, lastModified), null);
        }else{
            Error error = new Error(-3,"","","");
            listener.fault(error);
        }
    }

    private boolean downloadImpl()throws Throwable{

//                HttpGet get = new HttpGet();


        String md5s = MD5.digest(get.getURI().toString());


        String cacheFileName = "download-cache-" + md5s+new Date().getTime();


        file = new File(cacheFileName + ".cache");
        file.deleteOnExit();
        if(file.exists()){
            //                    fileInfo(url, null);
            HttpFileInfo info = getFileInfo(url);
            length = info.getFileSize();
            lastModified = info.getLastModified();
            if(file.length() == length && file.lastModified() == lastModified){
                return true;
            }
            file.delete();
        }

        File dFile = new File(cacheFileName + ".download");

        dFile.deleteOnExit();

        byte[] buffer = new byte[1024 * 4];
        do {
            downFile(get, dFile, buffer);
        }while ((statusCode == HttpStatus.SC_OK
                || statusCode == HttpStatus.SC_PARTIAL_CONTENT)
                && dFile.length() < length);

        if (length > 0 && dFile.length() != length) {
            //listener.fault(new Error(-3,null,null,null));
            dFile.delete();
            return false;
        }

        dFile.renameTo(file);
        file.setLastModified(lastModified);
//                    listener.result(file,null);
        return true;
//        } else {
////                    if(listener!=null){
////                        listener.fault(new Error(-3,null,null,null));
////                    }
//            return false;
////                    throw new RuntimeException("下载失败，服务器连接异常，状态码:" + response.getStatusLine().getStatusCode());
//        }
    }

    private void downFile(HttpGet get,File dFile,byte[] buffer)throws Throwable{
        long start = dFile.length();
        if (start > 0) {
//            conn.setRequestProperty("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
            get.setHeader("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
        } else {
            start = 0;
            get.setHeader("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
        }

        HttpClient http = genHttp();
        HttpResponse response = http.execute(get);

//        if (statusCode == HttpStatus.SC_OK) {
//            length = response
//            start = 0;
//        } else if (statusCode == HttpStatus.SC_PARTIAL) {
//            parseRange(conn.getHeaderField("Content-Range"));
//        }
        statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == HttpStatus.SC_OK
                ||statusCode == HttpStatus.SC_PARTIAL_CONTENT) {

            final HttpEntity entity = response.getEntity();
//            length = entity.getContentLength();
            if (statusCode == HttpStatus.SC_OK) {
                length = entity.getContentLength();
                start = 0;
            } else{
                parseRange(response.getLastHeader("Content-Range"));
            }
//                    fileName = response.get
            lastModified = parserLastModified(response);
            fileName = parserFileName(response);

            InputStream _in = entity.getContent();

//                    File file = new File("");


            //file = new File(file.getAbsoluteFile() + "/download-cache-" + md5s + ".cache");

            OutputStream _out = new FileOutputStream(dFile, true);


            int count = 0;
            long total = start;
            while ((count = _in.read(buffer)) != -1) {
                _out.write(buffer, 0, count);
                total += count;
//                        System.out.println("1 progress:"+total+"\ttotal:"+length);
                listener.progress(total, length);
            }
            _in.close();
        }
    }

    private void parseRange(Header header) {

        length = -1;
        if(header == null){
            return;
        }

        String range = header.getValue();
        if(range == null || range.length() < 6){
            return;
        }
        range = range.substring(6);
        String[] rs = range.split("/");

        length = Integer.parseInt(rs[1]);

        rs = rs[0].split("-");

//        rStart = Integer.parseInt(rs[0]);
//        rEnd = Integer.parseInt(rs[1]);
    }


    @Override
    public void setImpl(HttpCommunicateImpl impl) {
        this.impl = impl;
    }

    @Override
    public void setListener(ProgressResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void setParams(HttpCommunicate.Params params) {
        this.params = params;
    }
}
