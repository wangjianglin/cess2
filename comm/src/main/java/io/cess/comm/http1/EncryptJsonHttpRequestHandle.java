package io.cess.comm.http1;


import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.AbstractHttpMessage;

import io.cess.comm.Constants;
import io.cess.util.JsonUtil;
import io.cess.util.reflect.PropertyOperator;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:03:13
 *
 */
public class EncryptJsonHttpRequestHandle implements HttpRequestHandle {

	@Override
	public Map<String, Object> getParams(AbstractHttpMessage httpMessage,HttpPackage pack) {
		httpMessage.addHeader(Constants.HTTP_COMM_PROTOCOL, Constants.HTTP_VERSION);
		StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"location\":");
        sb.append("\"" + pack.getUrl() + "\",");
        sb.append("\"timestamp\":");
        sb.append(HttpUtils.getTimestamp());
        sb.append(",\"sequeueid\":");
        sb.append(HttpUtils.getSequeue());

        //增加版本信息
//        sb.append(",\"version\":{\"major\":");
//        sb.append(pack.getVersion().getMajor());
//        sb.append(",\"minor\":");
//        sb.append(pack.getVersion().getMinor());
//        sb.append("}");

        //增加参数信息
        sb.append(",\"data\":");
        Map<String, Object> dict = pack.getParams();
        //AD.Core.Web.Json.DataContractJsonSerializer dc = new AD.Core.Web.Json.DataContractJsonSerializer(typeof(Dictionary<string, object>));
        ////String jsonString = "{}";
        //MemoryStream ms = new MemoryStream();
        //dc.WriteObject(ms, dict);
        //byte[] tmp = ms.ToArray();
        //sb.append(Encoding.UTF8.GetString(tmp, 0, tmp.Length));
        
//            try {
				sb.append(JsonUtil.serialize(dict));
//			} catch (JSONException e1) {
//				e1.printStackTrace();
//			}
        
        
        //sb.append("{\"data\":\"测试中文\"}");

        sb.append("}");
        
        String json = sb.toString();
        //json = new sun.misc.BASE64Encoder().encodeBuffer(json.getBytes());
        json = Base64.getEncoder().encodeToString(json.getBytes());
//        try {
//			json =  java.net.URLEncoder.encode(json, System.getProperty("sun.jnu.encoding"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
        
        //String b64s = System.Convert.ToBase64String(Encoding.Default.GetBytes(sb.ToString()));
        //Console.WriteLine("value:" + sb.ToString());
        //Console.WriteLine("b64s:" + b64s);
        //Console.WriteLine("coding:" + Encoding.Default.EncodingName);
       // return "jsonParam=" + HttpUtility.UrlEncode(b64s)+
        //    "&coding=" + Encoding.Default.BodyName;
        Map<String,Object> map = new HashMap<>();
//        map.put("__jsonParam__", json);
//        map.put("__request_coding__", System.getProperty("file.encoding"));
//        map.put("__version__", "0.1");
//        map.put("__result__", "json");
        map.put(Constants.HTTP_JSON_PARAM, json);
        map.put(Constants.HTTP_REQUEST_CODING, System.getProperty("file.encoding"));
        //map.put(Constants.HTTP_VERSION, "0.1");
        //map.put(HttpRequest.URI, pack.getUri());
		return map;
	}

	@Override
	public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
		//System.out.println("result:"+resp);
//		jsonParam = URLDecoder.decode(jsonParam, "utf-8");
		//jsonParam = jsonParam;
		//byte[] tmpBs = new BASE64Decoder().decodeBuffer(jsonParam);
		String resp = null;
		try {
			resp = new String(bytes,"accsic");
			byte[] tmpBs = Base64.getDecoder().decode(resp);
			resp = new String(tmpBs,"utf-8");
//			resp = io.cessutil.Base64.decode(resp,"utf-8");
		} catch (Throwable e1) {
			e1.printStackTrace();
			Error error = new Error();
			error.setCode(-3);
			HttpUtils.fireFault(listener::fault, error);
			return;
		}
		
		Object obj = null;
		List<Error> warnings = null;
		Error error = null;
		try{
//			obj  = io.cessutil.json.JSONUtil.deserialize(resp);
//			ResultData resultData = (ResultData) io.cessutil.json.JSONUtil.deserialize(obj,ResultData.class);
//			///ResultData resltData = (ResultData) ad.util.json.JSONUtil.deserialize(resp, ResultData.class);
//			if(resultData.code <0){
//				error = new Error();
//				PropertyOperator.copy(resultData, error);
//			}else{
//				@SuppressWarnings("unchecked")
//				Map<String,Object> map = (Map<String, Object>) obj;
//				obj = io.cessutil.json.JSONUtil.deserialize(map.get("result"), pack.getRespType());
//				warning = resultData.getWarning();
//			}
			@SuppressWarnings("rawtypes")
			ResultData<?> resultData = (ResultData) JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, pack.getRespType()));
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
