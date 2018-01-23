package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.RespType;
import io.cess.util.Function;
import io.cess.comm.tcp.annotation.RespType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lin on 1/28/16.
 */
class PackageManagerImpl<K,Q extends RequestPackage,R extends ResponsePackage> {

    private Map<K,Class<Q>> requests = new HashMap<>();
    private Map<K,Class<R>> responses = new HashMap<>();
    private Map<String,K> responseKeys = new HashMap<>();

    private Function<K,Class<Q>> toKey;
    PackageManagerImpl(Function<K,Class<Q>> toKey){
        this.toKey = toKey;
    }

    void regist(Class<Q> cls){

        if(cls == null){
            return;
        }

        K key = toKey.function(cls);

        if (key == null){
            return;
        }

        requests.put(key,cls);

        Class<? extends ResponsePackage> respType = getRespType(cls);
        if(respType != null){
            responses.put(key, (Class<R>) respType);
            responseKeys.put(respType.getName(),key);
        }

    }

    private Class<? extends ResponsePackage> getRespType(Class<?> cls){
        if(cls == null
                || cls == RequestPackage.class
                || cls == Object.class ){
            return null;
        }

        RespType respType = cls.getAnnotation(RespType.class);

        if(respType != null){
            return respType.value();
        }

        return getRespType(cls.getSuperclass());
    }

//    private String clsToString(Class<?> cls){
//        return cls.getPackage()+":"+cls.getName();
//    }

    K request(Class<?> cls){
        return responseKeys.get(cls.getName());
    }

    Q newRequestInstance(K key){

        Class<Q> cls = requests.get(key);
        if (cls == null){
            return null;
        }
        try {
            return  cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    R newResponseInstance(K key){

        Class<R> cls = responses.get(key);
        if (cls == null){
            return null;
        }
        try {
            return  cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
