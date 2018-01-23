//package lin.web.action;
//
//import org.apache.struts2.convention.annotation.Action;
//import org.apache.struts2.convention.annotation.Actions;
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//
//import TestService;
//
//import com.opensymphony.xwork2.ActionContext;
//
//@Results( {
//		@Result(name = "next", location = "next.jsp", type = "cloud",params={"dispatcher","2"}),
//		@Result(name = "error", location = "error.jsp", type = "cloud"),
//		@Result(name = "redirectAction", location = "./child/child", type = "cloud",params={"redirectAction","2"}),
//		@Result(name = "redirect", location = "http://sunflowers.javaeye.com", type = "cloud",params={"redirect","2"}) })
//public class NavigatorAction {
//	private String actionName;
//
//	private TestService comm;
//	
//	public void setActionName(String actionName) {
//		this.actionName = actionName;
//	}
//
//	public String edit(){
//		return "next";
//	}
//	public String index() {
//		comm.test();
//		outputMsg("method:index");
//		return "next";
//	}
//
//	public String execute() {
//		outputMsg("method:execute,no index method");
//		return "next";
//	}
//
//	public String error() {
//		try {
//			throw new Exception();
//		} catch (Exception e) {
//			outputMsg(e);
//			return "error";
//		}
//	}
//
//	public String redirect() {
//		System.out.println("重定向：rediret");
//		return "redirect";
//	}
//
//	public String redirectAction() {
//		outputMsg("navigatorAction 跳转而来，原地址是navigator!redirectAction.action，请查看地址栏");
//		return "redirectAction";
//	}
//
//	@Action(value = "/test/childTest")
//	public String action() {
//		outputMsg("@action ---method:action");
//		return "next";
//	}
//
//	@Actions( { @Action(value = "/test/action1"),
//			@Action(value = "/test/action2") })
//	public String actions() {
//		outputMsg("@actions ---method:actions,action=" + actionName);
//		return "next";
//	}
//	private void outputMsg(Object msg) {
//		System.out.println(msg);
//		ActionContext.getContext().put("msg", msg);
//	}
//
//	public void setComm(TestService comm) {
//		this.comm = comm;
//	}
//}
