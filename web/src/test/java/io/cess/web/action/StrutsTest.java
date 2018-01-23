//package lin.web.action;
//
//import static org.junit.Assert.*;
//
//import org.apache.struts2.StrutsJUnit4TestCase;
//import org.apache.struts2.dispatcher.mapper.ActionMapping;
//import org.junit.Test;
//
//
///**
// * 
// * @author 王江林
// * @date 2012-7-5 下午2:22:10
// *
// */
////public class StrutsTest{
////public class StrutsTest extends StrutsSpringJUnit4TestCase<StrutsTest>{
//public class StrutsTest extends StrutsJUnit4TestCase<StrutsTest>{
//	
//	
//	
//		@Test
//	//对action的测试
//		public void testGetActionMapping() throws Exception {
//			ActionMapping mapping = getActionMapping("/web/action/navigator.action");
//			assertNotNull(mapping);
////			assertEquals("/begin", mapping.getNamespace());
////			assertEquals("helloWorld", mapping.getName());
//
//		}
//
//	    //对action的执行过程的一个模拟测试
//		public void testGetActionProxy() throws Exception {
//	  //set parameters before calling getActionProxy
//			request.setParameter("username", "FD");
//			//ActionProxy proxy = getActionProxy("/web/action/navigator.action");
//			//assertNotNull(proxy);
//
//			//NavigatorAction action = (NavigatorAction) proxy.getAction();
//			//assertNotNull(action);
//
//			//String result = proxy.execute();
//			//assertEquals(Action.SUCCESS, result);
//			//assertEquals("FD", action.getClass());
//		}
//}
