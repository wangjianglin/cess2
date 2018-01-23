package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Command;
import io.cess.comm.tcp.annotation.Command;

/**
 * Created by lin on 1/28/16.
 */
public class CommandPackageManager {

    private static PackageManagerImpl<Integer,CommandRequestPackage,CommandResponsePackage> impl = new
            PackageManagerImpl<>(cls ->{
        return getCommand(cls);
    });

    private static int getCommand(Class<?> cls){
        if(cls == null
                || cls == CommandRequestPackage.class
                || cls == Object.class ){
            return 0;
        }

        Command command = cls.getAnnotation(Command.class);

        if(command != null){
            return command.value();
        }

        return getCommand(cls.getSuperclass());
    }

    public static void regist(Class<? extends CommandRequestPackage> cls){
        impl.regist((Class<CommandRequestPackage>) cls);
    }

    public static int request(Class<? extends CommandResponsePackage> cls){
        Integer command = impl.request(cls);
        if(command == null){
            return 0;
        }
        return command;
    }

    public static CommandRequestPackage newRequestInstance(int key){

        return impl.newRequestInstance(key);
    }

    public static CommandResponsePackage newResponseInstance(int key){

        return impl.newResponseInstance(key);
    }
}
