package io.cess.comm.http1;


import java.util.List;
import io.cess.util.JsonUtil;
import io.cess.util.reflect.PropertyOperator;


/**
 * 
 * @author lin
 * @date Mar 8, 2015 3:01:41 PM
 *
 *
 */
public class StandardJsonHttpRequestHandle extends AbstractHttpRequestHandle{

	@SuppressWarnings("unchecked")
	@Override
	public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
//	public void response(io.cessclient.http1.HttpPackage pack, String resp, ResultListener listener){
		Object obj = null;
		List<Error> warnings = null;
		Error error = null;
		try{
			String resp = new String(bytes,"utf-8");
//			obj  = io.cessutil.json.JSONUtil.deserialize(resp);
//			ResultData resultData = (ResultData) io.cessutil.json.JSONUtil.deserialize(obj,ResultData.class);
//			///ResultData resltData = (ResultData) ad.util.json.JSONUtil.deserialize(resp, ResultData.class);
//			if(resultData.code <0){
//				error = new Error();
//				error.setCode(resultData.code);
//				error.setCause(resultData.cause);
//				error.setMessage(resultData.message);
//				error.setStackTrace(resultData.stackTrace);
//				PropertyOperator.copy(resultData, error);
//			}else{
//				@SuppressWarnings("unchecked")
//				Map<String,Object> map = (Map<String, Object>) obj;
//				obj = io.cessutil.json.JSONUtil.deserialize(map.get("result"), pack.getRespType());
//				warning = resultData.getWarning();
//			}
			
			@SuppressWarnings("rawtypes")
			ResultData resultData = (ResultData) JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, pack.getRespType()));
			///ResultData resltData = (ResultData) ad.util.json.JSONUtil.deserialize(resp, ResultData.class);
			if(resultData.code <0){
				error = new Error();
				error.setCode(resultData.code);
				error.setCause(resultData.cause);
				error.setMessage(resultData.message);
				error.setStackTrace(resultData.stackTrace);
				PropertyOperator.copy(resultData, error);
			}else{
//				Map<String,Object> map = (Map<String, Object>) obj;
				//obj = io.cessutil.json.JSONUtil.deserialize(map.get("result"), pack.getRespType());
//				Object obj2 = resultData.getResult();
				obj = resultData.getResult();
				warnings = resultData.getWarnings();
			}
		}catch(Throwable e){
			e.printStackTrace();
			error = new Error();
			error.setCode(-1);
			//return;
		}
		if(error != null){
			HttpUtils.fireFault(listener::fault, error);
		}else{
			HttpUtils.fireResult(listener::result,obj,warnings);
		}
	}
	public static class ResultData<T>{
		private long code;
		private long sequeueid;// { get; set; }
	        //public object result { get; set; }
		private String message;// { get; set; }
		private List<Error> warnings;// { get; set; }

		private String cause;// { get; set; }

		private String stackTrace;// { get; set; }

		private int dataType ;//{ get; set; }
		
		private T result;
	        
		public long getCode() {
			return code;
		}
		public void setCode(long code) {
			this.code = code;
		}
		public long getSequeueid() {
			return sequeueid;
		}
		public void setSequeueid(long sequeueid) {
			this.sequeueid = sequeueid;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		public List<Error> getWarnings() {
			return warnings;
		}
		public void setWarnings(List<Error> warnings) {
			this.warnings = warnings;
		}
		public String getCause() {
			return cause;
		}
		public void setCause(String cause) {
			this.cause = cause;
		}
		public String getStackTrace() {
			return stackTrace;
		}
		public void setStackTrace(String stackTrace) {
			this.stackTrace = stackTrace;
		}
		public int getDataType() {
			return dataType;
		}
		public void setDataType(int dataType) {
			this.dataType = dataType;
		}
		public T getResult() {
			return result;
		}
		public void setResult(T result) {
			this.result = result;
		}
	}
}
