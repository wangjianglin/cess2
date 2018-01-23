package io.cess.comm.http;

import io.cess.util.JsonUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Created by lin on 2/23/16.
 */
public class NormalHttpRequestHandle  extends AbstractHttpRequestHandle{

    @Override
    public void response(HttpPackage pack,HttpClientResponse response, ResultListener listener) {

        Object obj;
        try{
            String resp = new String(response.getData(),"utf-8");

            Type type =  pack.getRespType();

            if(type == String.class) {
                obj = resp;
            }else if(type == int.class || type == Integer.class){
                obj = Integer.parseInt(resp);
            }else if(type == long.class || type == Long.class){
                obj = Long.parseLong(resp);
            }else if(type == byte.class || type == Byte.class){
                obj = Byte.parseByte(resp);
            }else if(type == short.class || type == Short.class){
                obj = Short.parseShort(resp);
            }else if(type == float.class || type == Float.class){
                obj = Float.parseFloat(resp);
            }else if(type == double.class || type == Double.class){
                obj = Double.parseDouble(resp);
            }else if(type == BigDecimal.class){
                obj = new BigDecimal(resp);
            }else if(type == BigInteger.class){
                obj = new BigInteger(resp);
            }else {
                obj = JsonUtil.deserialize(resp, pack.getRespType());
            }
        }catch(Throwable e){
            e.printStackTrace();
            Error error = new Error(-1,null,null,null);
            HttpUtils.fireFault(listener, error);
            return;
        }

        HttpUtils.fireResult(listener,obj,null);

    }

}