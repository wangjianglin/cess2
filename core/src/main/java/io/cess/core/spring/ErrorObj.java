package io.cess.core.spring;

/**
 * Created by lin on 23/06/2017.
 */
public class ErrorObj {
    /**
     * 产生错误的具体原因，程序层面的
     */
    private String cause;// { get; internal set; }//
    private long code;// { get; internal set; }
    /**
     * 产生的错误消息，用于展示给用户的
     */
    private String message;// { get; internal set; }
    /**
     * 产生错误的堆栈信息
     */
    private String stackTrace;// { get; internal set; }

    private Object data ;//{ get; set; }

    private int dataType;// { get; set; }//数据类型,0、正常，1、后台验证错误

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}
