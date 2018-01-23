package io.cess.comm.httpdns;

import io.cess.util.JsonUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 4/25/16.
 */

public class AliHttpDNS extends AbstractHttpDNS{

    public class Data{
        private String host;
        private long ttl;
        private String[] ips;
    }
    private static String SERVER_IP = "203.107.1.1";

    public static void setServerIp(String ip){
        SERVER_IP = ip;
    }
    private String accountId;
    public AliHttpDNS(String account){
        this.accountId = account;
    }

    @Override
    protected HostObject fetch(String hostName,int timeout) {
                String resolveUrl = "http://" + SERVER_IP + "/" + accountId + "/d?host=" + hostName;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(resolveUrl).openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            if (conn.getResponseCode() != 200) {
//                    HttpDNSLog.logW("[QueryHostTask.call] - response code: " + conn.getResponseCode());
            } else {
                InputStream in = conn.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = streamReader.readLine()) != null) {
                    sb.append(line);
                }
                Data data = JsonUtil.deserialize(sb.toString(),Data.class);
//                JSONObject json = new JSONObject(sb.toString());
//                String host = json.getString("host");
                String host = data.host;
//                long ttl = json.getLong("ttl");
                long ttl = data.ttl;
                String[] ips = data.ips;
//                JSONArray ips = json.getJSONArray("ips");
                if (host != null) {

                    HostObject hostObject = new HostObject();

                    List<String> ipsList = new ArrayList<>();

                    String ipItem = null;
                    for(int n=0;n<ips.length;n++){
                        ipItem = ips[n];
                        if(ipItem == null || "".equals(ipItem)){
                            continue;
                        }
                        ipsList.add(ipItem);
                    }

                    hostObject.setHostName(host);
                    hostObject.setTtl(ttl);
                    hostObject.setIps(ipsList.toArray(new String[]{}));

                    return hostObject;
                }
            }
        } catch (Exception e) {
//                if (HttpDNSLog.isLogEnabled()) {
//                    e.printStackTrace();
//                }
        }
        return null;
    }
}
//public class AliHttpDNS implements HttpDNS {
//
//    private static final String SERVER_IP = "203.107.1.1";
//    private String accountId = "";
//    private static final int MAX_THREAD_NUM = 5;
//    private static final int RESOLVE_TIMEOUT_IN_SEC = 10;
//    private static final int MAX_HOLD_HOST_NUM = 100;
//    private static final int EMPTY_RESULT_HOST_TTL = 30;
//
//    private boolean isExpiredIpAvailable = true;
//
//
//    private class HostObject {
//
//        @Override
//        public String toString() {
//            return "HostObject [hostName=" + hostName + ", ip=" + ip + ", ttl=" + ttl + ", queryTime="
//                    + queryTime + "]";
//        }
//
//        private String hostName;
//        private String ip;
//        private long ttl;
//        private long queryTime;
//
//        public boolean isExpired() {
//            return getQueryTime() + ttl < System.currentTimeMillis() / 1000;
//        }
//
//        public String getIp() {
//            return ip;
//        }
//
//        public void setIp(String ip) {
//            this.ip = ip;
//        }
//
//        public void setHostName(String hostName) {
//            this.hostName = hostName;
//        }
//
//        public String getHostName() {
//            return hostName;
//        }
//
//        public long getTtl() {
//            return ttl;
//        }
//
//        public void setTtl(long ttl) {
//            this.ttl = ttl;
//        }
//
//        public long getQueryTime() {
//            return queryTime;
//        }
//
//        public void setQueryTime(long queryTime) {
//            this.queryTime = queryTime;
//        }
//    }
//
//    class QueryHostTask implements Callable<String> {
//        private String hostName;
//        private boolean isRequestRetried = false;
//
//        public QueryHostTask(String hostToQuery) {
//            this.hostName = hostToQuery;
//        }
//
//        @Override
//        public String call() {
//            String resolveUrl = "http://" + SERVER_IP + "/" + accountId + "/d?host=" + hostName;
////            HttpDNSLog.logD("[QueryHostTask.call] - buildUrl: " + resolveUrl);
//            try {
//                HttpURLConnection conn = (HttpURLConnection) new URL(resolveUrl).openConnection();
//                conn.setConnectTimeout(RESOLVE_TIMEOUT_IN_SEC * 1000);
//                conn.setReadTimeout(RESOLVE_TIMEOUT_IN_SEC * 1000);
//                if (conn.getResponseCode() != 200) {
////                    HttpDNSLog.logW("[QueryHostTask.call] - response code: " + conn.getResponseCode());
//                } else {
//                    InputStream in = conn.getInputStream();
//                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = streamReader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    JSONObject json = new JSONObject(sb.toString());
//                    String host = json.getString("host");
//                    long ttl = json.getLong("ttl");
//                    JSONArray ips = json.getJSONArray("ips");
//                    if (host != null) {
//                        if (ttl == 0) {
//                            // 如果有结果返回，但是ip列表为空，ttl也为空，那默认没有ip就是解析结果，并设置ttl为一个较长的时间
//                            // 避免一直请求同一个ip冲击sever
//                            ttl = EMPTY_RESULT_HOST_TTL;
//                        }
//                        HostObject hostObject = new HostObject();
//                        String ip = (ips == null) ? null : ips.getString(0);
////                        HttpDNSLog.logD("[QueryHostTask.call] - resolve host:" + host + " ip:" + ip + " ttl:" + ttl);
//                        hostObject.setHostName(host);
//                        hostObject.setTtl(ttl);
//                        hostObject.setIp(ip);
//                        hostObject.setQueryTime(System.currentTimeMillis() / 1000);
//                        if (hostManager.size() < MAX_HOLD_HOST_NUM) {
//                            hostManager.put(hostName, hostObject);
//                        }
//                        return ip;
//                    }
//                }
//            } catch (Exception e) {
////                if (HttpDNSLog.isLogEnabled()) {
////                    e.printStackTrace();
////                }
//            }
//            if (!isRequestRetried) {
//                isRequestRetried = true;
//                return call();
//            }
//            return null;
//        }
//    }
//
//    private ConcurrentMap<String, HostObject> hostManager = new ConcurrentHashMap<String, HostObject>();
////    private static HttpDNS instance = new HttpDNS();
//    private ExecutorService pool = Executors.newFixedThreadPool(MAX_THREAD_NUM);
//    private DegradationFilter degradationFilter = null;
//    private Context context;
//
//    public AliHttpDNS(Context context, String accountId) {
//        this.accountId = accountId;
//        this.context = context;
////        this.setDegradationFilter(new DegradationFilter() {
////            @Override
////            public boolean shouldDegradeHttpDNS(String hostName) {
////                // 此处可以自定义降级逻辑，例如www.taobao.com不使用HttpDNS解析
////                // 参照HttpDNS API文档，当存在中间HTTP代理时，应选择降级，使用Local DNS
////                return Utils.detectIfProxyExist(context);
////            }
////        });
//    }
//
//    // 是否允许过期的IP返回
//    public void setExpiredIpAvailable(boolean flag) {
//        isExpiredIpAvailable = flag;
//    }
//
//    public boolean isExpiredIpAvailable() {
//        return isExpiredIpAvailable;
//    }
//
//    public void setPreResolveHosts(List<String> hosts){
//        for(String hostName : hosts){
//            HostObject host = hostManager.get(hostName);
//            if (host == null || (host.isExpired() && !isExpiredIpAvailable())) {
//                pool.submit(new QueryHostTask(hostName));
//            }
//        }
//    }
//    public String getIpByHost(String hostName) {
////        if(Utils.detectIfProxyExist(context)) {
////            return null;
////        }
//        if (degradationFilter != null) {
//            if (degradationFilter.shouldDegradeHttpDNS(hostName)) {
//                return null;
//            }
//        }
//        HostObject host = hostManager.get(hostName);
//        if (host == null || (host.isExpired() && !isExpiredIpAvailable())) {
////            HttpDNSLog.logD("[getIpByHost] - fetch result from network, host: " + hostName);
//            Future<String> future = pool.submit(new QueryHostTask(hostName));
//            try {
//                return future.get();
//            } catch (Exception e) {
////                if (HttpDNSLog.isLogEnabled()) {
////                    e.printStackTrace();
////                }
//            }
//            return null;
//        } else if (host.isExpired()) {
//            // 同步返回，异步更新
////            HttpDNSLog.logD("[getIpByHost] - fetch result from cache, host: " + hostName);
//            pool.submit(new QueryHostTask(hostName));
//            return host.getIp();
//        }
////        HttpDNSLog.logD("[getIpByHost] - fetch result from cache, host: " + hostName);
//        return host.getIp();
//    }
//
//    public void setDegradationFilter(DegradationFilter filter) {
//        degradationFilter = filter;
//    }
//}
//
//
////
////package alibaba.httpdns_api_demo;
////
////        import android.util.Log;
////
////        import org.json.JSONArray;
////        import org.json.JSONObject;
////
////        import java.io.BufferedReader;
////        import java.io.InputStream;
////        import java.io.InputStreamReader;
////        import java.net.HttpURLConnection;
////        import java.net.URL;
////        import java.util.concurrent.Callable;
////        import java.util.concurrent.ConcurrentHashMap;
////        import java.util.concurrent.ConcurrentMap;
////        import java.util.concurrent.ExecutorService;
////        import java.util.concurrent.Executors;
////        import java.util.concurrent.Future;
////
////public class HttpDNS {
////
////    private static final String SERVER_IP = "203.107.1.1";
////    private static final String ACCOUNT_ID = "139450";
////    private static final int MAX_THREAD_NUM = 5;
////    private static final int RESOLVE_TIMEOUT_IN_SEC = 10;
////    private static final int MAX_HOLD_HOST_NUM = 100;
////    private static final int EMPTY_RESULT_HOST_TTL = 30;
////
////    private boolean isExpiredIpAvailable = false;
////
////    public static class HttpDNSLog {
////
////        private static final String TAG = "HttpDNS";
////        private static boolean enableLog;
////
////        /**
////         * 打开log观察调试信息
////         */
////        public static void enableLog(boolean enable) {
////            enableLog = enable;
////        }
////
////        /**
////         * @return if log is enabled, return true, else false.
////         */
////        public static boolean isLogEnabled() {
////            return enableLog;
////        }
////
////        /**
////         * verbose级别log
////         *
////         * @param msg message to print out.
////         */
////        protected static void logV(String msg) {
////            if (enableLog) {
////                Log.v(TAG, msg);
////            }
////        }
////
////        /**
////         *
////         * @param msg message to print out.
////         */
////        protected static void logD(String msg) {
////            if (enableLog) {
////                Log.d(TAG, msg);
////            }
////        }
////
////        /**
////         * info级别log
////         *
////         * @param msg message to print out.
////         */
////        protected static void logI(String msg) {
////            if (enableLog) {
////                Log.i(TAG, msg);
////            }
////        }
////
////        /**
////         * warning级别log
////         *
////         * @param msg message to print out.
////         */
////        protected static void logW(String msg) {
////            if (enableLog) {
////                Log.w(TAG, msg);
////            }
////        }
////
////        /**
////         * error级别log
////         *
////         * @param msg message to print out.
////         */
////        protected static void logE(String msg) {
////            if (enableLog) {
////                Log.e(TAG, msg);
////            }
////        }
////    }
////
////    class HostObject {
////
////        @Override
////        public String toString() {
////            return "HostObject [hostName=" + hostName + ", ip=" + ip + ", ttl=" + ttl + ", queryTime="
////                    + queryTime + "]";
////        }
////
////        private String hostName;
////        private String ip;
////        private long ttl;
////        private long queryTime;
////
////        public boolean isExpired() {
////            return getQueryTime() + ttl < System.currentTimeMillis() / 1000;
////        }
////
////        public String getIp() {
////            return ip;
////        }
////
////        public void setIp(String ip) {
////            this.ip = ip;
////        }
////
////        public void setHostName(String hostName) {
////            this.hostName = hostName;
////        }
////
////        public String getHostName() {
////            return hostName;
////        }
////
////        public long getTtl() {
////            return ttl;
////        }
////
////        public void setTtl(long ttl) {
////            this.ttl = ttl;
////        }
////
////        public long getQueryTime() {
////            return queryTime;
////        }
////
////        public void setQueryTime(long queryTime) {
////            this.queryTime = queryTime;
////        }
////    }
////
////    class QueryHostTask implements Callable<String> {
////        private String hostName;
////        private boolean isRequestRetried = false;
////
////        public QueryHostTask(String hostToQuery) {
////            this.hostName = hostToQuery;
////        }
////
////        @Override
////        public String call() {
////            String resolveUrl = "http://" + SERVER_IP + "/" + ACCOUNT_ID + "/d?host=" + hostName;
////            HttpDNSLog.logD("[QueryHostTask.call] - buildUrl: " + resolveUrl);
////            try {
////                HttpURLConnection conn = (HttpURLConnection) new URL(resolveUrl).openConnection();
////                conn.setConnectTimeout(RESOLVE_TIMEOUT_IN_SEC * 1000);
////                conn.setReadTimeout(RESOLVE_TIMEOUT_IN_SEC * 1000);
////                if (conn.getResponseCode() != 200) {
////                    HttpDNSLog.logW("[QueryHostTask.call] - response code: " + conn.getResponseCode());
////                } else {
////                    InputStream in = conn.getInputStream();
////                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
////                    StringBuilder sb = new StringBuilder();
////                    String line;
////                    while ((line = streamReader.readLine()) != null) {
////                        sb.append(line);
////                    }
////                    JSONObject json = new JSONObject(sb.toString());
////                    String host = json.getString("host");
////                    long ttl = json.getLong("ttl");
////                    JSONArray ips = json.getJSONArray("ips");
////                    if (host != null) {
////                        if (ttl == 0) {
////                            // 如果有结果返回，但是ip列表为空，ttl也为空，那默认没有ip就是解析结果，并设置ttl为一个较长的时间
////                            // 避免一直请求同一个ip冲击sever
////                            ttl = EMPTY_RESULT_HOST_TTL;
////                        }
////                        HostObject hostObject = new HostObject();
////                        String ip = (ips == null) ? null : ips.getString(0);
////                        HttpDNSLog.logD("[QueryHostTask.call] - resolve host:" + host + " ip:" + ip + " ttl:" + ttl);
////                        hostObject.setHostName(host);
////                        hostObject.setTtl(ttl);
////                        hostObject.setIp(ip);
////                        hostObject.setQueryTime(System.currentTimeMillis() / 1000);
////                        if (hostManager.size() < MAX_HOLD_HOST_NUM) {
////                            hostManager.put(hostName, hostObject);
////                        }
////                        return ip;
////                    }
////                }
////            } catch (Exception e) {
////                if (HttpDNSLog.isLogEnabled()) {
////                    e.printStackTrace();
////                }
////            }
////            if (!isRequestRetried) {
////                isRequestRetried = true;
////                return call();
////            }
////            return null;
////        }
////    }
////
////    private ConcurrentMap<String, HostObject> hostManager = new ConcurrentHashMap<String, HostObject>();
////    private static HttpDNS instance = new HttpDNS();
////    private ExecutorService pool = Executors.newFixedThreadPool(MAX_THREAD_NUM);
////    private DegradationFilter degradationFilter = null;
////
////    private HttpDNS() {
////    }
////
////    public static HttpDNS getInstance() {
////        return instance;
////    }
////
////    // 是否允许过期的IP返回
////    public void setExpiredIpAvailable(boolean flag) {
////        isExpiredIpAvailable = flag;
////    }
////
////    public boolean isExpiredIpAvailable() {
////        return isExpiredIpAvailable;
////    }
////
////    public String getIpByHost(String hostName) {
////        if (degradationFilter != null) {
////            if (degradationFilter.shouldDegradeHttpDNS(hostName)) {
////                return null;
////            }
////        }
////        HostObject host = hostManager.get(hostName);
////        if (host == null || (host.isExpired() && !isExpiredIpAvailable())) {
////            HttpDNSLog.logD("[getIpByHost] - fetch result from network, host: " + hostName);
////            Future<String> future = pool.submit(new QueryHostTask(hostName));
////            try {
////                return future.get();
////            } catch (Exception e) {
////                if (HttpDNSLog.isLogEnabled()) {
////                    e.printStackTrace();
////                }
////            }
////            return null;
////        } else if (host.isExpired()) {
////            // 同步返回，异步更新
////            HttpDNSLog.logD("[getIpByHost] - fetch result from cache, host: " + hostName);
////            pool.submit(new QueryHostTask(hostName));
////            return host.getIp();
////        }
////        HttpDNSLog.logD("[getIpByHost] - fetch result from cache, host: " + hostName);
////        return host.getIp();
////    }
////
////    public void setDegradationFilter(DegradationFilter filter) {
////        degradationFilter = filter;
////    }
////}
