package io.cess.comm.tcp;

import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by lin on 1/21/16.
 */
public class IOSTest {

    @Test
    public void testIOS() throws Throwable{
        Socket socket = new Socket("192.168.1.103",567);
        InputStream _in = socket.getInputStream();
        OutputStream _out = socket.getOutputStream();

        _out.write("hello!".getBytes());

        byte[] bs = new byte[1024];
        int count = _in.read(bs);

        System.out.println(new String(bs,0,count));
    }
}
