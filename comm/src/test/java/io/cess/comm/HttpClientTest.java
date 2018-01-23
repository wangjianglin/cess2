package io.cess.comm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientTest {

	@org.junit.Test
	public void testHttpClient() throws Exception{
		///DefaultHttpClient http = new DefaultHttpClient();
		CloseableHttpClient http = HttpClients.createDefault();
		  HttpGet get = new HttpGet("http://192.168.1.18:8080/web/extjs-4.1.1/ext-all.gzip");
		  
		  //get.addHeader("accept-encoding", "gzip,deflate");
		  //get.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; Alexa Toolbar; Maxthon 2.0)");
		  //http.setCredentialsProvider(credsProvider)
		  //http.setCookieStore(cookieStore)
		  HttpResponse response = http.execute(get);
		  HttpEntity entity = response.getEntity();

	        // If the response does not enclose an entity, there is no need
	        // to bother about connection release
	        System.out.println("count:"+entity.getContentLength());
	        InputStream instream = entity.getContent();
	        //byte[] data = new byte[(int)response.getEntity().getContentLength()];
	        byte[] data = new byte[100024];
	        int count = 0;
	        int total = 0;
	        
	        new File("c:\\tmp.gzip").createNewFile();
	        try(OutputStream _out = new FileOutputStream(new File("c:\\tmp.gzip"))){
		        while((count = instream.read(data))!=-1){
		        	total+=count;
		        	_out.write(data, 0, count);
		        }
	        }
	        System.out.println("data:"+total);
	        

	        //System.out.println("data:"+new String(data,"utf-8"));
	}
}
