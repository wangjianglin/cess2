package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.ProtocolParserType;
import io.cess.util.ByteUtil;


@ProtocolParserType(1)
public class CommandProtocolParser_010 extends AbstractProtocolParser
{
//    private static Map<Integer, Class<?>> commands = new HashMap<Integer, Class<?>>();

    static
    {
//        commands.put(1, DetectPackage.class);
        CommandPackageManager.regist(DetectPackage.class);
    }

    public TcpPackage getPackage()
    {

        CommandPackage pack = null;
        try {
            int command = ByteUtil.readInt(buffer,3);
//            pack = (CommandPackage) commands.get(command).newInstance();
            if (state == PackageState.REQUEST){
                pack = CommandPackageManager.newRequestInstance(command);
            }else{
                pack = CommandPackageManager.newResponseInstance(command);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        pack.parse(buffer,11);
//        pack.setMajor((byte)0);
//        pack.setMinor((byte)1);
//        pack.setRevise((byte)0);
        return pack;
    }

}
