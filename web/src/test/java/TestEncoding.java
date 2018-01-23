
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author 王江林
 * @date 2012-8-30 下午7:52:39
 *
 */
public class TestEncoding {

	@Test
	public void testEncoding() throws Exception{
		
		showBase64("测试中文！");
		showBase64("{\"location\":\"/cloud/action/comm!test.action\",\"timestamp\":3423423423,\"sequeueid\":3423048023,\"version\":{\"major\":0,\"minor\":0},\"data\":{\"data\":\"测试中文！\"}}");
	}
	
	@Test 
	public void testUrl() throws Exception{
		String str = "%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F%28ppt%E5%90%88%E9%9B%86%29";
		str = URLDecoder.decode(str,"utf-8");
		System.out.println("str:"+str);
	}
	
	private static void showBase64(String s){
		s = new BASE64Encoder().encode(s.getBytes());
		//String s = new String(bs);
		System.out.println("base 64:"+s);
	}
	
	@Test
	public void testDecoding() throws Exception{
		String str = "{\"location\":\"/cloud/action/comm!test.action\",\"timestamp\":3423423423,\"sequeueid\":3423048023,\"version\":{\"major\":0,\"minor\":0},\"data\":{\"data\":\"测试中文！\"}}";
		str = (new BASE64Encoder()).encode(str.getBytes());
		str = URLEncoder.encode(str, "ascii");
		System.out.println("str:"+str);
		//str = "eyJjb2RlIjoxMzIxMDAsIm1lc3NhZ2UiOiLpobXpnaLkuI3lrZjlnKjvvIEiLCJjYXVzZSI6Iumh%0AtemdouS4jeWtmOWcqO%2B8gSIsInN0YWNrVHJhY2UiOm51bGx9%0A";
		str = "MagicDraw+UML+Enterprise+16.8+32bit%E7%A0%B4%E8%A7%A3%E8%A1%A5%E4%B8%81";
		str = URLDecoder.decode(str,"ascii");
		System.out.println("str:"+str);
		str = new String((new BASE64Decoder()).decodeBuffer(str),"utf-8");
		
		System.out.println("str:"+str);
	}
	
	@Test
	public void testNumber(){
		System.out.println("value:"+Long.parseLong("-6000002",16));
	}
}
