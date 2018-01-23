package io.cess.comm.http1;

import java.util.Map;

public class SessionIdPackage extends HttpPackage {

	public SessionIdPackage(){
		super("/core/comm/sessionId.action");
	}
	
	 @Override
	public Map<String, Object> getParams() {
		return null;
	}

}
