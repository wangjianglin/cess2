package io.cess.comm.http2;

import java.net.URL;
import java.util.List;

import io.cess.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

public class HttpProxyTest {

	@Before
	public void testBefore()throws Exception{
//		HttpCommunicate.setCommUrl(new URL("http://www.as-6.com:9080/lin.demo"));
//		HttpCommunicate.setCommUrl(new URL("http://www.feicuibaba.com"));
//		HttpCommunicate.setCommUrl(new URL("http://127.0.0.1:9080/proxy"));
		HttpCommunicate.setCommUrl(new URL("http://112.117.223.27:18080/"));
		HttpCommunicate.setDebug(true);
	}
	
	@Test
	public void testProxy(){
		GoodsPackage pack = new GoodsPackage();
		pack.setRecommand(3);
		pack.setCurrentPage(4);
		
		for(int n=0;n<20;n++){
		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
//			testEntityResult = (TestEntity) obj;
			System.out.println("obj:"+ JsonUtil.serialize(obj));
			},error -> {
				System.out.println(error.getStackTrace());
				System.out.println(error.getMessage());
			}
		).waitForEnd();
		
//		try {
//			//Thread.sleep(0);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		}
	}
}
