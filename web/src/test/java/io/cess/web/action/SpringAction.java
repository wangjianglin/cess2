//package lin.web.action;
//
//import org.apache.struts2.convention.annotation.Action;
//import org.apache.struts2.convention.annotation.Results;
//import org.apache.struts2.convention.annotation.Result;
//
//import TestService;
//
///**
// * 
// * @author 王江林
// * @date 2012-7-19 下午10:32:03
// *
// */
//@Results(value={
//		@Result(name="test",type="cloud",params={"root","null"})
//})
//public class SpringAction {
//
//	private TestService service;
//	
//	@Action
//	public String test(){
//		service.test();
//		return "test";
//	}
//
//	public void setService(TestService service) {
//		this.service = service;
//	}
//}
