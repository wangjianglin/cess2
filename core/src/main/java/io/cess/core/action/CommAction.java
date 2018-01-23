//package lin.core.action;
//
//
//import javax.servlet.http1.HttpServletRequest;
//
//import org.apache.struts2.StrutsStatics;
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//
//import com.opensymphony.xwork2.ActionContext;
//
//
//import lin.CessException;
//
///**
// * 
// * @author 王江林
// * @date 2012-3-13 下午5:49:19
// * 
// *
// */
//@Results({
//	@Result(name="sendAction",type="redirectAction",location="${location}"),
//	@Result(name="send",type="cloud-comm",location="${location}",params={"encode","false"}),
//	@Result(name="sendRedirect",type="redirect",location="${location}"),
//	@Result(name="test",type="cloud",params={"root","data"}),//用于测试
//	@Result(name="noparamError",type="cloud-json"),
//	@Result(name="listener",type="redirect",location="${location}")
//})
//public class CommAction {
//
//	/**
//	 * 
//	 */
//	//private Map<String,String> params;
//	/**
//	 * 
//	 */
//	//private String jsonParam;
//	/**
//	 * 
//	 */
//	private String location;
//	/**
//	 * 
//	 */
//	private String coding = "utf-8";
//	/**
//	 * 测试数据
//	 */
//	private String data;
//	
//	public String test(){
//		System.out.println("data:"+data);
////		AdException e = new AdException(0x1_0101);
////		AdException.add(e);
////		e = new AdException(0x1_0102);
////		AdException.add(e);
////		e = new AdException(0x1_0103);
////		AdException.add(e);
//		return "test";
//	}
//	
//	public String sessionId(){
//		ActionContext actionContext = ActionContext.getContext().getActionInvocation().getInvocationContext();
//        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
//       
//        this.data = request.getSession().getId();
//		return "test";
//	}
//	public String adException(){
//		throw new CessException(-0x1_0101);
//	}
//	
//	public String exception(){
//		throw new RuntimeException();
//	}
//	
//	
////	public String send(){
////		//
////		//params = "{\"name\":\"name\",\"password\":\"password\"}";
////		//params = "{\"name\":\"\",\"password\":\"password\"}";
////		//对数据进行处理,如数据加密与解密，数据验证，防重复提交等
////		//暂时没有处理
////		String tmpJsonParams = null;
////		if(jsonParam != null){
////			try {
////				//tmpJsonParams = URLDecoder.decode(jsonParam, "accsic");
////				tmpJsonParams = jsonParam;
////				byte[] bs = new BASE64Decoder().decodeBuffer(tmpJsonParams);
////				tmpJsonParams = new String(bs,coding);
////				//System.out.println("json:"+tmpJsonParams);
////				tmpJsonParams = new String(tmpJsonParams.getBytes(),"utf-8");
////				//System.out.println("json:"+tmpJsonParams);
////			} catch (Throwable e) {
////				e.printStackTrace();
////			}
////		}
////		try {
////			if(tmpJsonParams == null || "".equals(tmpJsonParams)){
////				throw new AdException(-2);
////			}
////			Object obj = JSONUtil.deserialize(tmpJsonParams);
////			if(obj == null){
////				//throw new CessException(0xA00103,"客户端向服务器传输的数据为空！");
////			}
////			if(obj instanceof Map){
////				@SuppressWarnings("unchecked")
////				Map<String,String> tmpParams = (Map<String,String>)obj;
////				if(tmpParams != null &&tmpParams.containsKey("location")){
////					location = tmpParams.get("location");
////					if(location!= null && (location.startsWith("/cloud/action/comm!send.action") || location.startsWith("comm!send.action"))){//不允许的请求参数
////						throw new AdException(-1);
////					}
////					if(tmpParams.containsKey("version")){
////						Object tmpVersion = tmpParams.get("version");
////						
////						@SuppressWarnings("unchecked")
////						Map<String, Long> map = (Map<String,Long>)tmpVersion;
////						Version version = new Version();
////						version.setMajor(((Long)map.get("major")).intValue());
////						version.setMinor(((Long)map.get("minor")).intValue());
////						
////						
////						if(!location.startsWith("/")){//在location前面加上"/"
////							location = "/"+location;
////						}
////						
////						//根据 版本信息 处理URL
////						if(version.getMajor() != 0 || version.getMinor()!=0){
////							int index = location.indexOf('/',1);
////							StringBuffer tmpLocation = new StringBuffer();
////							tmpLocation.append(location.subSequence(0, index));
////							tmpLocation.append('_');
////							tmpLocation.append(version.getMajor());
////							tmpLocation.append('_');
////							tmpLocation.append(version.getMinor());
////							tmpLocation.append(location.substring(index));
////							location = tmpLocation.toString();
////						}
////					}
////				}else{
////					return "noparamError";
////				}
////				if(tmpParams != null && tmpParams.containsKey("data")){
////					params = new HashMap<String, String>();
////					JSONToParameters.processesParameters(tmpParams.get("data"),params,null);
////				}else{
////					//异常
////				}
////				//processesParameters(tmp.get("data"),result,null);
////				//processesParameters(tmp,result,null);
////				//ActionContext.getContext().put(sequeueid, tmp.get(sequeueid));
////			}
////		} catch (JSONException e) {
////			System.out.println("json:"+tmpJsonParams);
////			e.printStackTrace();
////			//throw new CessException(0xA00101,"数据格式异常！");
////		}
////		
////		if(params == null){
////			params = new HashMap<String, String>();
////		}
////		params.put("__coding__", this.coding);
////		return "send";
//////		
//////		Map<String,String> tmpParams = JSONToParameters.toParameters(tmpJsonParams);
//////		
//////		if(tmpParams != null && tmpParams.containsKey("data")){
//////			param
//////		}
//////		if(this.location.indexOf(".action") != -1){
//////			return "sendAction";
//////		}else{
//////			return "sendRedirect";
//////		}
////	}
//	
//	public String listener(){
//		return "";
//	}
//
//	public String getLocation() {
//		return location;
//	}
//
//	public void setLocation(String location) {
//		this.location = location;
//	}
//
//	
//	public String getData() {
//		return data;
//	}
//	public void setData(String data) {
//		this.data = data;
//	}
//	public String getCoding() {
//		return coding;
//	}
//	public void setCoding(String coding) {
//		this.coding = coding;
//	}
//}
