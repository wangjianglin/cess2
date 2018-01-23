//package lin.web.action;
//
//import lin.CessException;
//
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//
//
///**
// * 
// * @author 王江林
// * @date 2012-7-20 下午3:24:08
// * 
// * 用于测试throw异常后，如何捕获到，并转相应的jsp文件
// *
// */
//
//@Results(value={
//	@Result(name="ad",location="/test.jsp",type="cloud")
//})
//public class ExceptionAction {
//
//	private boolean exception = false;
//	public String ad(){
//		if(exception){
//			throw new CessException(-0x1l);
//		}
//		return "ad";
//	}
//	
//	public String none(){
//		if(exception){
//			int n = 5/0;
//			System.out.println("n:"+n);
//		}
//		return "none";
//	}
//	
//	public boolean isException() {
//		return exception;
//	}
//	public void setException(boolean exception) {
//		this.exception = exception;
//	}
//}
