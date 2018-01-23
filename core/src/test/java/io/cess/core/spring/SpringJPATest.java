//package lin.core.spring;
//
//import java.io.IOException;
//
//import javax.inject.Inject;
//
//import lin.core.entity.TestEntity;
//import lin.core.services.TestService;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:/META-INF/lin/lin-spring-bean.xml")
//public class SpringJPATest {
//
//	@Inject
//	private TestService service;
//	@Test
//	public void testJPA() throws IOException{
//		System.out.println("service:"+service);
//		service.save(new TestEntity());
//		System.in.read();
//	}
//}
