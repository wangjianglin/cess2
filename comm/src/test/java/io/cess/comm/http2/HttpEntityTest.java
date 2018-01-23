package io.cess.comm.http2;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cess.comm.http2.annotation.HttpPackageReturnType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author lin
 * @date Jul 2, 2015 11:52:34 PM
 *
 */
public class HttpEntityTest {

	
	@Before
	public void before() throws MalformedURLException{
		HttpCommunicate.setCommUrl(new URL(TestConstants.HTTP_COMM_URL));
		HttpCommunicate.setDebug(true);
	}
	
	private String testPathVariableResult = null;
	@Test
	public void testPathVariable(){
		PathVariablePackage pack = new PathVariablePackage();
		pack.path = "6";
		
		HttpCommunicate.request(pack, ((Object obj, List<Error> warning)-> {
				testPathVariableResult = (String) obj;
			}),(error -> {
				System.out.println(error.getStackTrace());
			})).waitForEnd();

//		HttpCommunicate.request(pack,((obj1, warning1) -> {
//
//		}),(error1 -> {
//
//		})).waitForEnd();
		
		Assert.assertEquals("@PathVariable 失败！", testPathVariableResult, pack.path);
		
		
		pack.path = "string";
		
		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
				testPathVariableResult = (String) obj;
			},error -> {
				System.out.println(error.getStackTrace());
			}).waitForEnd();
		
		Assert.assertEquals("@PathVariable 字符串失败！", testPathVariableResult, pack.path);
		
		pack.path = "中文！";
		
		HttpCommunicate.request(pack, (Object obj, List<Error> warning)-> {
				testPathVariableResult = (String) obj;
			},error -> {
			}).waitForEnd();
		
		Assert.assertEquals("@PathVariable 中文字符串失败！", testPathVariableResult, pack.path);
	}
	
	
	private TestEntity testEntityResult = null;
	@Test
	public void testEntity() throws URISyntaxException, MalformedURLException{
		HttpEntityTestPackge pack = new HttpEntityTestPackge();
		TestEntity entity = new TestEntity();
		entity.setId(3l);
		entity.setName("name");
		entity.setValue("value");
		entity.setDate(new Date());
		
		TestEntity data = new TestEntity();
		data.setId(3l);
		data.setName("name");
		data.setValue("value");
		data.setDate(new Date());
		entity.setData(data);
		pack.setEntity(entity);
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(1l);
		ids.add(2l);
		ids.add(3l);
		ids.add(4l);
		ids.add(5l);
		ids.add(6l);
		entity.setIds(ids);
		data.setIds(ids);
		
		HttpCommunicate.request(pack, ((Object obj, List<Error> warning)-> {
				testEntityResult = (TestEntity) obj;
			}),((error) -> {
				System.out.println(error.getStackTrace());
				System.out.println(error.getMessage());
			})
		).waitForEnd();

		
		Assert.assertNotNull(testEntityResult);

		Assert.assertEquals(testEntityResult.getId(), entity.getId());
		Assert.assertEquals(testEntityResult.getName(), entity.getName());
		Assert.assertEquals(testEntityResult.getValue(), entity.getValue());
		Assert.assertEquals(testEntityResult.getDate().getTime(), entity.getDate().getTime());
		

		Assert.assertEquals(testEntityResult.getData().getId(), entity.getData().getId());
		Assert.assertEquals(testEntityResult.getData().getName(), entity.getData().getName());
		Assert.assertEquals(testEntityResult.getData().getValue(), entity.getData().getValue());
		Assert.assertEquals(testEntityResult.getData().getDate().getTime(), entity.getData().getDate().getTime());
	}
}

//@HttpPackageUrl("/savePathVariable/6")
@HttpPackageReturnType(String.class)
class PathVariablePackage extends HttpPackage{
	String path;
	
	@Override
	public String getUrl() {
		return "/test/savePathVariable/"+path+".action";
	}
}
class HttpEntityTestPackge extends HttpPackage {

	public HttpEntityTestPackge() {
		super("/test/saveEntity.action");
		this.setRespType(TestEntity.class);
	}

	private TestEntity entity;
//	private List<Long> ids;
	@Override
	public Map<String, Object> getParams() {
		Map<String,Object> map = new HashMap<>();
		map.put("entity",entity);
//		map.put("ids",ids);
		return map;
	}
	public TestEntity getEntity() {
		return entity;
	}
	public void setEntity(TestEntity entity) {
		this.entity = entity;
	}
//	public List<Long> getIds() {
//		return ids;
//	}
//	public void setIds(List<Long> ids) {
//		this.ids = ids;
//	}
	
}
