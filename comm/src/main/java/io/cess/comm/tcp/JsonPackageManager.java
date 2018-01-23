package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Path;
import io.cess.comm.tcp.annotation.Path;

/**
 * Created by lin on 1/28/16.
 */
public class JsonPackageManager {
    private static PackageManagerImpl<String,JsonRequestPackage,JsonResponsePackage> impl = new
            PackageManagerImpl<>(cls ->{
        return getPath(cls);
    });

    private static String getPath(Class<?> cls){
        if(cls == null
                || cls == CommandRequestPackage.class
                || cls == Object.class ){
            return "";
        }

        Path path = cls.getAnnotation(Path.class);

        if(path != null){
            return path.value();
        }

        return getPath(cls.getSuperclass());
    }

    public static void regist(Class<? extends JsonRequestPackage> cls){
        impl.regist((Class<JsonRequestPackage>) cls);
    }

    public static String request(Class<? extends JsonResponsePackage> cls){
        String path = impl.request(cls);
        if(path == null || "".equals(path)){
            return "";
        }
        return path;
    }

    public static JsonRequestPackage newRequestInstance(String key){

        return impl.newRequestInstance(key);
    }

    public static JsonResponsePackage newResponseInstance(String key){

        return impl.newResponseInstance(key);
    }
}
