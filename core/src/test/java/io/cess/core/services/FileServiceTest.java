//package lin.core.services;
//
//import java.io.File;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Persistence;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import lin.core.services.impl.FileServiceImpl;
//
//public class FileServiceTest {
//
//	private EntityManager jpa;
//
//	@Test
//	public void testUpload(){
//		FileServiceImpl fileService = new FileServiceImpl();
//		fileService.setJpa(jpa);
//		File file = new File("E:\\music\\最炫民族风 - 凤凰传奇.mp3");
//		fileService.upload(file, "最炫民族风.mp3", "video/h264");
//	}
//
//	@Before
//	public void before(){
//		jpa = Persistence.createEntityManagerFactory("cloud").createEntityManager();
//		jpa.getTransaction().begin();
//	}
//
//	@After
//	public void after(){
//		jpa.getTransaction().commit();
//	}
//}
