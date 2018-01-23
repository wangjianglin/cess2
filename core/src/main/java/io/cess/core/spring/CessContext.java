package io.cess.core.spring;

import org.springframework.web.method.HandlerMethod;

public class CessContext {

    private static ThreadLocal<CessContextValue> threadLocal = new ThreadLocal<>();

    public static void set(){
        threadLocal.set(new CessContextValue());
    }

    public static HandlerMethod getHandlerMethod(){
        return threadLocal.get().handlerMethod;
    }

    public static void setHandlerMethod(HandlerMethod handlerMethod){
        threadLocal.get().handlerMethod = handlerMethod;
    }
    private static class CessContextValue{

        private HandlerMethod handlerMethod;
    }
}
