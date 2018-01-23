package io.cess.comm.http2;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.cess.comm.http1.annotation.HttpPackageReturnType;
import io.cess.comm.http1.annotation.HttpPackageUrl;
import io.cess.comm.http1.annotation.HttpParamName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * @author lin
 * @date Jul 2, 2015 11:52:53 PM
 *
 */
public class ListTcpPackageTest {

	@Before
	public void before() throws MalformedURLException{
		HttpCommunicate.setCommUrl(new URL(TestConstants.HTTP_COMM_URL));
		HttpCommunicate.setDebug(true);
	}

	private List<String> stringIdsResult = null;
	private List<Long> longIdsResult = null;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testListPackage(){
		StringListPackage stringListPack = new StringListPackage();
		List<String> stringIds = new ArrayList<>();
		stringIds.add("0");
		stringIds.add("1");
		stringIds.add("2");
		stringIds.add("a");
		stringIds.add("b");
		stringIds.add("c");
		stringIds.add("+");
		stringIds.add("01234abc+@");
		stringIds.add("01234abc+@中文数据");
		stringListPack.setIds(stringIds);
		
		HttpCommunicate.request(stringListPack, (Object obj, List<Error> warning)-> {
			stringIdsResult = (List<String>)obj;
		},error -> {
			System.out.println(error.getStackTrace());
		}).waitForEnd();
		
		Assert.assertEquals(stringIds.size(), stringIdsResult.size());
		for(int n=0;n<stringIds.size();n++){
			Assert.assertEquals(stringIds.get(n), stringIdsResult.get(n));
		}
		
		
		LongListPackage longListPack = new LongListPackage();
		List<Long> longIds = new ArrayList<>();
		longIds.add(0l);
		longIds.add(1l);
		longIds.add(2l);
		longIds.add(10l);
		longIds.add(20l);
		longIds.add(-10l);
		longIds.add(90l);
		longListPack.setIds(longIds);
		
		HttpCommunicate.request(longListPack, (Object obj, List<Error> warning)-> {
			longIdsResult = (List<Long>)obj;
		},error -> {
		}).waitForEnd();
		
		Assert.assertEquals(longIds.size(), longIdsResult.size());
		for(int n=0;n<longIds.size();n++){
			Assert.assertEquals(longIds.get(n), longIdsResult.get(n));
		}
		
	}
}

@HttpPackageUrl("/test/saveStringList.action")
@HttpPackageReturnType(value=List.class,parameterizedType={String.class})
class StringListPackage extends HttpPackage{
	
	@HttpParamName
	private List<String> ids;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
}

@HttpPackageUrl("/test/saveLongList.action")
@HttpPackageReturnType(value=List.class,parameterizedType={Long.class})
class LongListPackage extends HttpPackage{
	
	@HttpParamName
	private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
