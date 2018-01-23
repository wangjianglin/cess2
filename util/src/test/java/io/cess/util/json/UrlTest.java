package io.cess.util.json;

import java.net.URLDecoder;

import org.junit.Test;

public class UrlTest {

	@Test
	public void testUrl() throws Exception{
		System.out.println("url:"+URLDecoder.decode("C:\\Documents%20and%20Settings\\Administrator\\workspace2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\web\\WEB-INF\\lib", "utf-8"));
	}
}
