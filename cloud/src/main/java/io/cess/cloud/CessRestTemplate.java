package io.cess.cloud;

import io.cess.CessException;
import io.cess.cloud.mock.RestMock;
import io.cess.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.*;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CessRestTemplate extends ResourceRestTemplate{

    public static enum Version {

        V_1,V_2;

        @Override
        public String toString() {
            if(this == V_2){
                return "0.2";
            }
            return "0.1";
        }
    }

    public static final String HTTP_COMM_PROTOCOL = "__http_comm_protocol__";
    public static final String HTTP_COMM_WITH_WARNING = "__http_comm_with_warning__";
    public static final String HTTP_COMM_WITH_ERROR = "__http_comm_with_erro__";
    public static final String HTTP_COMM_PROTOCOL_DEBUG = "__http_comm_protocol_debug__";

    private Version version = Version.V_2;

    @Value("${io.cess.debug:false}")
    private boolean debug = false;

    @Autowired(required = false)
    private List<RestMock> restMocks;

    public void setVersion(Version version){
        this.version = version;
        this.setMessageConverter();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public CessRestTemplate() {
        super();
        this.setMessageConverter();
    }

    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        final ClientHttpRequest request = super.createRequest(url, method);

        request.getHeaders().add(HTTP_COMM_PROTOCOL, this.version.toString());

        if(debug){
            request.getHeaders().add(HTTP_COMM_PROTOCOL_DEBUG, "");
        }

        return request;
    }

    private void setMessageConverter(){
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        if(this.version == Version.V_1) {
            messageConverters.add(new CessMessageConverterV1());
        }else{
            messageConverters.add(new CessMessageConverterV2());
        }

        this.setMessageConverters(messageConverters);
    }

    @Override
    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                              @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException{
        RequestCallback newRequestCallback = requestCallback;
        if(restMocks != null && !restMocks.isEmpty()){
            Object result = null;
            for(RestMock restMock : restMocks){
                result = restMock.mock(url,method);
                if(result != null){
                    break;
                }
            }
            if(result instanceof RestMock.Null){
                return null;
            }

            if(result != null){
                return (T) result;
            }

            newRequestCallback = request -> {
                Object result1 = null;
                for(RestMock restMock : restMocks){
                    result1 = restMock.mock(request);
                    if(result1 != null){
                        break;
                    }
                }
                if(result1 instanceof RestMock.Null){
                    throw new RestMock.Exception(null);
                }
                if(result1 != null){
                    throw new RestMock.Exception(request);
                }
                requestCallback.doWithRequest(request);
            };
        }

        try {
            return super.doExecute(url, method, newRequestCallback, responseExtractor);
        }catch (RestMock.Exception e){
            return (T) e.getValue();
        }
    }

    public static class ResultData<T>{
        private long code;
        private long sequeueid;// { get; set; }
        //public object result { get; set; }
        private String message;// { get; set; }
        private List<Error> warning;// { get; set; }

        private String cause;// { get; set; }

        private String stackTrace;// { get; set; }

        private int dataType ;//{ get; set; }

        private T result;

        public long getCode() {
            return code;
        }
        public void setCode(long code) {
            this.code = code;
        }
        public long getSequeueid() {
            return sequeueid;
        }
        public void setSequeueid(long sequeueid) {
            this.sequeueid = sequeueid;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public List<Error> getWarning() {
            return warning;
        }
        public void setWarning(List<Error> warning) {
            this.warning = warning;
        }
        public String getCause() {
            return cause;
        }
        public void setCause(String cause) {
            this.cause = cause;
        }
        public String getStackTrace() {
            return stackTrace;
        }
        public void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }
        public int getDataType() {
            return dataType;
        }
        public void setDataType(int dataType) {
            this.dataType = dataType;
        }
        public T getResult() {
            return result;
        }
        public void setResult(T result) {
            this.result = result;
        }
    }

    public static class Error {

        /**
         * 产生错误的具体原因，程序层面的，如：NullPointException
         */
        private String cause;

        /**
         * 错误的唯一标识码，用于精确定位错误源
         */
        private long code;
        /**
         * 产生的错误消息，用于展示给用户的，如：密码错误
         */
        private String message;

        /**
         * 用跟踪错误的堆栈信息
         */
        private String stackTrace;

        /**
         * 错误发生时，伴随的与错误相关的数据
         */
        private Object data;

        /**
         * 错误类型,0、由后台业务逻辑错误产生，1、后台请求前置验证不通过产生的错误，2、HTTP请求由于网络方面的原因产生的错误
         */
        private int type = 3;

        private List<Error> warning;

        /**
         * @hide
         */
        Error(){}

        public Error(long code,String message,String cause,String stackTrace){
            this.code = code;
            this.message = message;
            this.cause = cause;
            this.stackTrace = stackTrace;
        }

        public Error(long code,String message,String cause,String stackTrace,int type,Object data){
            this.code = code;
            this.message = message;
            this.cause = cause;
            this.stackTrace = stackTrace;
            this.type = type;
            this.data = data;
        }

        public long getCode() {
            return code;
        }

        void setCode(long code) {
            this.code = code;
        }

        public String getCause() {
            return cause;
        }

        void setCause(String cause) {
            this.cause = cause;
        }

        public String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }

        public Object getData() {
            return data;
        }

        void setData(Object data) {
            this.data = data;
        }

        public int getType() {
            return type;
        }

        void setType(int type) {
            this.type = type;
        }

        public List<Error> getWarning() {
            return warning;
        }

        void setWarning(List<Error> warning) {
            this.warning = warning;
        }
    }

    class CessMessageConverterV1 extends AbstractGenericHttpMessageConverter<Object> {

        CessMessageConverterV1(){
            this.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        }

        @Override
        protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        }

        @Override
        protected Object readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

            return null;
        }

        @Override
        public Object read(Type type, Class contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            ClientHttpResponse response = (ClientHttpResponse) inputMessage;

            String resp = Utils.toString(response);

            ResultData resultData = JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, type));

            return processResultData(resultData);
        }
    }

    private static Object processResultData(ResultData resultData){
        if(resultData.code <0){
            throw new CessException(resultData.code,resultData.message,new RuntimeException(resultData.stackTrace));
        }

        List<Error> warnings = resultData.warning;
        if(warnings != null) {
            for(Error warning : warnings) {
                CessException.add(new CessException(warning.code,warning.message,new RuntimeException(warning.stackTrace)));
            }
        }

        return resultData.result;
    }

    class CessMessageConverterV2 extends AbstractGenericHttpMessageConverter<Object> {

        CessMessageConverterV2(){
            this.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        }

        @Override
        protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        }

        @Override
        protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            return null;
        }

        @Override
        public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            ClientHttpResponse response = (ClientHttpResponse) inputMessage;
            Object result = null;
            if (response.getStatusCode() == HttpStatus.OK) {
                if(response.getHeaders().get(HTTP_COMM_WITH_ERROR) != null){
                    error(Utils.toString(response));
                }else {
                    String resp = Utils.toString(response);
                    if (response.getHeaders().get(HTTP_COMM_WITH_WARNING) != null) {
                        result = resultWithWarning(type, resp);
                    } else {
                        result = result(type, resp);
                    }
                }
            }
            return result;
        }

        private Object resultWithWarning(Type type,String resp){

            ResultData resultData = JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, type));

            return processResultData(resultData);
        }

        private Object result(Type type,String resp){
            return JsonUtil.deserialize(resp, type);
        }

        public void error(String resp){

            ResultData resultData = JsonUtil.deserialize(resp, ResultData.class);
            if(resultData.code >= 0){
                resultData.code = -1;
            }
            processResultData(resultData);
        }
    }

//
//    public static String parseCharset(String contentType, String defaultCharset) {
//        if(contentType != null) {
//            String[] params = contentType.split(";");
//
//            for(int i = 1; i < params.length; ++i) {
//                String[] pair = params[i].trim().split("=");
//                if(pair.length == 2 && pair[0].equals("charset")) {
//                    return pair[1];
//                }
//            }
//        }
//
//        return defaultCharset;
//    }
}


//class ResponseExtractor<T> implements org.springframework.web.client.ResponseExtractor<T> {
//
//    @Override
//    public T extractData(ClientHttpResponse response) throws IOException {
//        return null;
//    }
//}