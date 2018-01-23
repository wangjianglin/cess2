package io.cess.core;

/**
 * 
 * @author 王江林
 * @date 2012-7-13 下午3:55:43
 *
 */
public class EncodingTest {

	//@Test
	public void testEncoding() throws Exception{
		String s = new String("测试中文".getBytes("utf-8"));
		System.out.println("测试："+s);
	}
	
	public static void main(String[] args) throws Exception{
		new EncodingTest().testEncoding();
	}
}
