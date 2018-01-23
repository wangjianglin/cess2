package io.cess.cloud;

import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class Utils {

    public static String toString(ClientHttpResponse response)throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bs = new byte[4096];
        int count = 0;
        while ((count = response.getBody().read(bs)) != -1){
            out.write(bs,0,count);
        }

        Charset charset = response.getHeaders().getContentType().getCharset();
        return new String(out.toByteArray(),charset != null?charset:Charset.forName("utf-8"));
    }
}
