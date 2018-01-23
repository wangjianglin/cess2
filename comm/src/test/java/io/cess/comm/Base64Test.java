package io.cess.comm;

import java.io.IOException;
import java.util.Base64;

import org.junit.Test;

public class Base64Test {

	@Test
	public void testBase64() throws IOException{
		String _json = "{\"location\":\"/cloud/action/comm!test.action\",\"timestamp\":1400216622932,\"sequeueid\":-9223372036854775807,\"version\":{\"major\":0,\"minor\":0},\"data\":{\"data\":\"测试中文！\"}}";
        String json = Base64.getEncoder().encodeToString(_json.getBytes());
        System.out.println("base 64:"+json);
        byte[] bs = Base64.getDecoder().decode(json);
        
        System.out.println("json:"+new String(bs));
        
        json = Base64.getEncoder().encodeToString(_json.getBytes());
        System.out.println("base 64:"+json);
        bs = Base64.getDecoder().decode(json);
        
        System.out.println("json:"+new String(bs));
	}
}
