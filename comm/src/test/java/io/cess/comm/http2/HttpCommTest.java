package io.cess.comm.http2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author lin
 * @date Jul 2, 2015 12:11:49 AM
 *
 */
public class HttpCommTest {

	@Before
	public void testBefore()throws Exception{
		HttpCommunicate.setCommUrl(new URL(TestConstants.HTTP_COMM_URL));
		HttpCommunicate.setDebug(true);
	}
	
	@Test
	public void testSessionId()throws Exception{
		SessionIdPackage sessionPack = new SessionIdPackage();
		class TmpResultListener implements ResultListener {
			private String sessionId = "";
			private boolean isSuccess = false;
			public TmpResultListener(){
			}
			@Override
			public void result(Object obj, List<Error> warning) {
				if(obj == null || "".equals(obj) || !(obj instanceof String)){
					isSuccess = false;
					return;
				}
				sessionId = (String) obj;
				System.out.println("session id:" + sessionId);
				isSuccess = true;
			}
			
			@Override
			public void fault(Error error) {
				sessionId = null;
				isSuccess = false;
			}
		};
		
		TmpResultListener listener = new TmpResultListener();
		String preSessionId = null;
		for(int n=0;n<10;n++){
			HttpCommunicate.request(sessionPack, listener).waitForEnd();
			Assert.assertTrue("获取session失败，或两次的session不一致！", listener.isSuccess && (preSessionId == null || preSessionId.equals(listener.sessionId)));
			preSessionId = listener.sessionId;
		}
		HttpCommunicateImpl client = HttpCommunicate.get("client");
		client.setCommUrl(new URL(TestConstants.HTTP_COMM_URL));
		preSessionId = null;
		for(int n=0;n<10;n++){
			client.request(sessionPack, listener).waitForEnd();
			Assert.assertTrue("获取session失败，或两次的session不一致！", listener.isSuccess && (preSessionId == null || preSessionId.equals(listener.sessionId)));
			preSessionId = listener.sessionId;
		}

		preSessionId = null;
		for(int n=0;n<10;n++){
			HttpCommunicate.get("client"+n).setCommUrl(new URL(TestConstants.HTTP_COMM_URL));
			HttpCommunicate.get("client"+n).request(sessionPack, listener).waitForEnd();
			Assert.assertTrue("获取session失败，或两个不同的HttpCommunicateImpl对象返回相同的SessionId！", listener.isSuccess && (preSessionId == null || !preSessionId.equals(listener.sessionId)));
			preSessionId = listener.sessionId;
		}
		
	}
	
	
	private String testCommResult = null;
	@Test
	public void testComm()throws IOException, URISyntaxException{
		TestPackage pack = new TestPackage();

		pack.setData("test!");
		
		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
			testCommResult = (String) obj;
			},error -> {
			}
		).waitForEnd();
		

		Assert.assertEquals("通信测试失败！",testCommResult,pack.getData());

		pack.setData("测试中文数据！");
		
		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
			testCommResult = (String) obj;
			},error -> {
			}
		).waitForEnd();
		

		Assert.assertEquals("通信传递中文失败！",testCommResult,pack.getData());
		
		pack.setData(null);
		
		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
			testCommResult = (String) obj;
			},error -> {
				Assert.assertTrue("返回null值失败！", false);
			}
		).waitForEnd();
		
		Assert.assertEquals("返回null值失败！",testCommResult,pack.getData());
		
		
		
		//System.in.read();
	}
	
	@Test
	public void testNull()throws IOException, URISyntaxException{
//		TestNullPackage pack = new TestNullPackage();
//
//
//		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
//			},error -> {
//				Assert.assertTrue("test返回值void的方法失败！", false);
//			}
//		).waitForEnd();

//		Object v = JsonUtil.deserialize("\"resp\"",String.class);
		HttpDataPackage dataPackage = new HttpDataPackage("http://suyun.58.com/download/guestdownload/?ch=mindex&amp;os=ios",HttpMethod.GET);
//		dataPackage.add("url","https://www.feicuibaba.com/proxy.php");
//		dataPackage.setRespType(Dwz.class);

		HttpCommunicate.request(dataPackage, new ResultListener() {
			@Override
			public void result(Object obj, List<Error> warning) {
				System.out.println(obj);
			}

			@Override
			public void fault(Error error) {
				System.out.println(error);
			}
		}).waitForEnd();
	}
}
