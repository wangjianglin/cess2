package io.cess.core.spring;

import io.cess.CessException;
import io.cess.core.Constants;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class Utils {

    private static String getProtocolVersion(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if("0.2".equals(request.getHeader(Constants.HTTP_COMM_PROTOCOL))){
            return "0.2";
        }
        if("0.1".equals(request.getHeader(Constants.HTTP_COMM_PROTOCOL))){
            return "0.1";
        }

        String accept = request.getHeader("Accept");
        if(accept == null || "".equals(accept.trim())){
            return "0.1";
        }

        if(accept.indexOf(Constants.MEDIA_TYPE_CESS2) != -1){
            return "0.2";
        }
        return "0.1";
    }

    static Object writeMessage(Object obj, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        if(obj == CessBody.NULL_VALUE){
            obj = null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean debug = request.getHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG) != null;
        String ver = getProtocolVersion();
        outputMessage.getHeaders().set(Constants.HTTP_COMM_PROTOCOL,ver);
        if("0.2".equals(ver)){
            return writeInternal02(obj, outputMessage,debug);
        }else{
            return writeInternal01(obj, outputMessage,debug);
        }
    }

    static private Object writeInternal02(Object object, HttpOutputMessage outputMessage,boolean debug){
        List<ErrorObj> warning = warningList(debug);
        if(warning == null || warning.isEmpty()){
            return object;
        }

        outputMessage.getHeaders().add(Constants.HTTP_COMM_WITH_WARNING, "");

        ResultObj02 result = new ResultObj02();
        result.setWarning(warning);
        result.setResult(object);

        return result;
    }

    static private Object writeInternal01(Object obj, HttpOutputMessage outputMessage, boolean debug)throws IOException,
            HttpMessageNotWritableException {

        ResultObj01 resultObj = new ResultObj01();
        resultObj.setResult(obj);
        resultObj.setCode(0);

        List<ErrorObj> errors = warningList(debug);
        resultObj.setWarnings(errors);

        return resultObj;
    }

    static private List<ErrorObj> warningList(boolean debug){
        List<ErrorObj> errors = new ArrayList<ErrorObj>();

        List<CessException> warnings = CessException.get();
        if(warnings != null) {
            ErrorObj error;
            for (CessException warning : warnings) {
                error = new ErrorObj();
                errors.add(error);
                if (debug) {
                    if (warning.getCause() != null) {
                        error.setCause(warning.getCause().getMessage());
                    }
                    error.setStackTrace(stackTrace(warning));
                }
                error.setMessage(warning.getMessage());
                error.setCode(warning.getCode());
            }
        }
        return errors;
    }

    static private String stackTrace(Throwable e){
        if(e == null){
            return null;
        }
        ByteArrayOutputStream _out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(_out));

        return _out.toString();

    }

    @XmlRootElement(name="cess")
    static public class ResultObj01{

        private Object result;
        private long code;

        private List<ErrorObj> warnings;

        public Object getResult() {
            return result;
        }
        public void setResult(Object result) {
            this.result = result;
        }
        public long getCode() {
            return code;
        }
        public void setCode(long code) {
            this.code = code;
        }
        public List<ErrorObj> getWarnings() {
            return warnings;
        }
        public void setWarnings(List<ErrorObj> warnings) {
            this.warnings = warnings;
        }
    }

    @XmlRootElement(name="cess")
    static public class ResultObj02{

        private Object result;

        private List<ErrorObj> warning;

        @XmlElement(name = "result")
        public Object getResult() {
            return result;
        }
        public void setResult(Object result) {
            this.result = result;
        }

        @XmlElement(name = "warning")
        public List<ErrorObj> getWarning() {
            return warning;
        }

        public void setWarning(List<ErrorObj> warning) {
            this.warning = warning;
        }
    }
}
