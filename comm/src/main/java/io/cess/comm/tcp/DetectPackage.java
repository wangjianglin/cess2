package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Command;
import io.cess.comm.tcp.annotation.RespType;
import io.cess.comm.tcp.annotation.Command;
import io.cess.comm.tcp.annotation.RespType;

@Command(1)
@RespType(DetectRespPackage.class)
public class DetectPackage extends CommandRequestPackage {

	@Override
	public void parse(byte[] bs,int offset) {
	}

	public DetectPackage()
	{
	}

}
