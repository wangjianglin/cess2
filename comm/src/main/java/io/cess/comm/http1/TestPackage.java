package io.cess.comm.http1;

import java.util.HashMap;
import java.util.Map;

public class TestPackage extends io.cess.comm.http1.HttpPackage{

	public TestPackage(){
		super("/core/comm/test.action");
	}
	
	 @Override
	public Map<String, Object> getParams() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data", data);
		return map;
	}

	private String data;// { get; set; }
//     public override IDictionary<string, object> GetParams()
//     {

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
         
}
