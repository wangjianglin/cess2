package io.cess.comm.http;

import io.cess.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lin
 * @date Jun 26, 2015 11:25:34 AM
 *
 */
public abstract class AbstractHttpRequestHandle implements HttpRequestHandle{

	@Override
	public void preprocess(HttpPackage pack,HttpCommunicate.Params params){}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParams(HttpPackage pack) {

		Map<String,Object> params = pack.getParams();
		if(params == null){
			return null;
		}
		Map<String,Object> textParams = new HashMap<String,Object>();
		Map<String,Object> contentParams = new HashMap<String,Object>();
		Object item = null;
		for(String key:params.keySet()){
			item = params.get(key);
			if(item instanceof FileParamInfo){
				contentParams.put(key, item);
			}else{
				textParams.put(key, item);
			}
		}
		@SuppressWarnings("rawtypes")
//		Map map = io.cessutil.json.JSONUtil.toParameters(textParams);
		Map map = JsonUtil.toParameters(textParams);
		map.putAll(contentParams);
		return map;
	}
}
