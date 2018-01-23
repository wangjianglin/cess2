package io.cess.web;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import io.cess.util.JsonUtil;
import io.cess.CessException;
import io.cess.comm.http.Error;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.annotation.HttpPackageReturnType;

public class ProxyFilter implements javax.servlet.Filter {


	private static ProxyHttpRequestHandle handle = new ProxyHttpRequestHandle();
	
	@HttpPackageReturnType(String.class)
	private class HttpProxyPackage extends HttpPackage{

		private Map<String,Object> params = null;
		
		HttpProxyPackage(String url){
			super(url);
			super.setRequestHandle(handle);
		}
		@Override
		public Map<String, Object> getParams() {
			return params;
		}
		
		
	}
	
//	private String contextPath;// = request.getServletContext().getContextPath();
	private int contextPathLength = 0;
	private URL url = null;
	private String encoding = "utf-8";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String contextPath = filterConfig.getServletContext().getContextPath();
		if(contextPath != null){
			contextPathLength = contextPath.length();
		}

		String urlString = filterConfig.getInitParameter("url");
		if(urlString == null){
			throw new CessException(-0x3_0001,"必须设置url属性");
		}
		try{
			url = new URL(urlString);
		}catch(Throwable e){
			throw new CessException(-0x3_0002,"url属性值无效");
		}
		
		String encodingString = filterConfig.getInitParameter("encoding");
		if(encodingString != null && !"".equals(encodingString.trim())){
			encoding = encodingString;
		}
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		request.setCharacterEncoding(encoding);
//		if(request.getRequestURI().endsWith(".action")){
			HttpCommunicateImpl http = io.cess.comm.http.HttpCommunicate.get(request.getRequestedSessionId());
			HttpProxyPackage pack = new HttpProxyPackage(request.getRequestURI().substring(contextPathLength));
			Map<String,String[]> map = request.getParameterMap();
			Map<String,Object> params = new HashMap<>();
			
			if(map != null){
				String[] value = null;
				for(String key : map.keySet()){
					value = map.get(key);
					if(value != null && value.length == 1){
						params.put(key, value[0]);
					}else{
						params.put(key, value);
					}
				}
			}
			
			pack.params = params;
//			System.out.println("===============================");
			
			http.setCommUrl(url);
			http.request(pack, (Object obj, List<Error> warning)-> {
//				testCommResult = (String) obj;
				try {
					response.getOutputStream().write((byte[])obj);
//					System.out.println("----------------------------");
				} catch (Exception e) {
					e.printStackTrace();
				}
				},error -> {
					try {
//						System.out.println("********************");
						response.getOutputStream().write(JsonUtil.serialize(error).getBytes());
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			).waitForEnd();
//		}
	}

	@Override
	public void destroy() {
		
	}

}
