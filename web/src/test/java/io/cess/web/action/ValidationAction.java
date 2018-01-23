//package lin.web.action;
//
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//import org.apache.struts2.interceptor.validation.SkipValidation;
//
//import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
//import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
//import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
//import com.opensymphony.xwork2.validator.annotations.Validations;
//import com.opensymphony.xwork2.validator.annotations.ValidatorType;
//
///**
// * 
// * @author 王江林
// * @date 2012-7-31 下午4:42:22
// *
// */
//@Validations(
//requiredStrings={
//		@RequiredStringValidator(fieldName="name1",message="用户名不能为空！---")
//}
//)
//@Results(value = { @Result(name="test1",location="/test1.jsp"),
//		@Result(name="input",location="/testinput.jsp",type="cloud",params={"exceptionCode","-0x600_0001","includeProperties","actionErrors*,actionMessages*,errors*,fieldErrors*"}),
//		@Result(name="test",location="/testinput.jsp",type="cloud",params={"exceptionCode","-0x600_0002","includeProperties","actionErrors.*,actionMessages.*,errors.*,fieldErrors.*"})
//})
////public class ValidationAction  extends ActionSupport{
//public class ValidationAction{
//
//	/**
//	 * 
//	 */
//	//private static final long serialVersionUID = 1L;
//
//	private String name1;
//	private String name2;
//	
//	@Validations(
//			requiredFields={
//					@RequiredFieldValidator(fieldName="name1",type=ValidatorType.FIELD,message="name1不能为空！"),
//					@RequiredFieldValidator(fieldName="name2",type=ValidatorType.FIELD,message="name2不能为空！")
//			}
//			)
//	@InputConfig(resultName="test")
//	public String test1(){
//		return "test1";
//	}
//	
//	@SkipValidation
//	public String test2(){
//		return "test1";
//	}
//	
//	@InputConfig(resultName="test2")
//	public String test3(){
//		return "test1";
//	}
//
//	public String getName1() {
//		return name1;
//	}
//
//	public void setName1(String name1) {
//		this.name1 = name1;
//	}
//
//	public String getName2() {
//		return name2;
//	}
//
//	public void setName2(String name2) {
//		this.name2 = name2;
//	}
//	
//}
