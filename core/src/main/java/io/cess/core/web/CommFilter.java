package io.cess.core.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import io.cess.CessException;
import io.cess.core.Constants;
import io.cess.util.JsonUtil;

/**
 * 
 * @author 王江林
 * @date 2012-8-16 上午11:51:40
 *
 */
public class CommFilter implements javax.servlet.Filter{

//	/**
//	 * 资源路径，默认为 /WEB-INF/content/
//	 */
//	private String resourcePath="/WEB-INF/content/";
//	private java.util.regex.Pattern[] patterns;
	
	
//	/**
//	 * 内容后缀
//	 */
//	private static final String CONTENT = "!__content__";
//	/**
//	 * 非内容后缀
//	 */
//	private static final String NOT_CONTENT = "!__not_content__";
	
//	/**
//	 * uri的属性名
//	 */
//	private static final String URI = "__uri__";
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		
		//解决线程池的问题，在使用线程池的时候，同一个线程有可能被多个请求使用
		CessException.claer();

		HttpServletRequest request = (HttpServletRequest)arg0;
		//System.out.println("session id:"+request.getSession().getId());
		String uri = request.getServletPath();
		HttpServletResponse response = (HttpServletResponse)arg1;

		if(Constants.HTTP_COMM_PROTOCOL_VERSION.equals(uri)){
			response.getOutputStream().write("[\"0.1.0build0\"]".getBytes());
			return;
		}
		String comm = request.getHeader(Constants.HTTP_COMM_PROTOCOL);
		if("0.2.1".equals(comm)){
			this.encryptFilter(request,response,chain);
//			String tmpJsonParams = request.getParameter(Constants.HTTP_JSON_PARAM);
//		//if(version != null){
//			if(tmpJsonParams != null && !"".equals(tmpJsonParams)){
//				Map<String,String[]> tmpMap =processEncryptData(request,tmpJsonParams);
//				HttpServletRequest r2 = new HttpServletRequestImpl(request,tmpMap);
//				//chain.doFilter(r2, new HttpServletResponseImpl(response));
//				chain.doFilter(r2, response);
//			}else{
//				throw new CessException(-2);
//			}
			return;
		}

		chain.doFilter(arg0, arg1);
		//}
	}

	private void encryptFilter(HttpServletRequest request, HttpServletResponse response,
							   FilterChain chain){

	}

	private Map<String,String[]> processEncryptData(HttpServletRequest request,String tmpJsonParams){

		Map<String,String[]> tmpMap = new HashMap<String, String[]>();	
		String coding = request.getParameter(Constants.HTTP_REQUEST_CODING);
		if(coding == null || "".equals(coding)){
			coding = "utf-8";
		}
	
		try {
			//byte[] bs = new BASE64Decoder().decodeBuffer(tmpJsonParams);
			byte[] bs = Base64.getDecoder().decode(tmpJsonParams);
			tmpJsonParams = new String(bs,coding);
//			tmpJsonParams = new String(tmpJsonParams.getBytes(),"utf-8");
		} catch (Throwable e) {
			throw new CessException(-3,"客户端向服务器传输的数据为空！");
		}
		Map<String, String> params=null;
		try {
			if(tmpJsonParams == null || "".equals(tmpJsonParams)){
				throw new CessException(-2);
			}
			Object obj = JsonUtil.deserialize(tmpJsonParams);
			if(obj == null){
				throw new CessException(-4);
			}
			if(obj instanceof Map){
				@SuppressWarnings("unchecked")
				Map<String,String> tmpParams = (Map<String,String>)obj;
//					if(tmpParams != null &&tmpParams.containsKey(LOCATION)){
//						location = tmpParams.get(LOCATION);
//					}
//					location = request.getParameter(URI);
//				if(location== null){//不允许的请求参数
//					throw new CessException(-1);
//				}
//				if(tmpParams.containsKey("version")){
//					Object tmpVersion = tmpParams.get("version");
//					
//					@SuppressWarnings("unchecked")
//					Map<String, Long> map = (Map<String,Long>)tmpVersion;
//					Version version = new Version();
//					version.setMajor(((Long)map.get("major")).intValue());
//					version.setMinor(((Long)map.get("minor")).intValue());
//					
//					
//					if(!location.startsWith("/")){//在location前面加上"/"
//						location = "/"+location;
//					}
//				
//				//根据 版本信息 处理URL
//					if(version.getMajor() != 0 || version.getMinor()!=0){
//						int index = location.indexOf('/',1);
//						StringBuffer tmpLocation = new StringBuffer();
//						tmpLocation.append(location.subSequence(0, index));
//						tmpLocation.append('_');
//						tmpLocation.append(version.getMajor());
//						tmpLocation.append('_');
//						tmpLocation.append(version.getMinor());
//						tmpLocation.append(location.substring(index));
//						location = tmpLocation.toString();
//					}
//				}
				
				if(tmpParams != null && tmpParams.containsKey("data")){
//					params = new HashMap<String, String>();
//					JSONToParameters.processesParameters(tmpParams.get("data"),params,null);
					params = JsonUtil.toParameters(tmpParams.get("data"));
					for(Map.Entry<String, String> entry : params.entrySet()){
						tmpMap.put(entry.getKey(), new String[]{entry.getValue()});
					}

					tmpMap.put("__coding__", new String[]{coding});
				}
			}
		}catch (Throwable e) {
			System.out.println("json:"+tmpJsonParams);
			e.printStackTrace();
			throw new CessException(-5);
		}
		return tmpMap;
	}
	@Override
	public void init(FilterConfig config) throws ServletException {
		
		//String htmlPath = config.getInitParameter("resourcePath");
//		String htmlPath = config.getServletContext().getInitParameter("resourcePath");
//		if(htmlPath != null && !"".equals(htmlPath)){
//			this.resourcePath = htmlPath;
//		}
//		if(!resourcePath.startsWith("/")){
//			resourcePath = "/" + resourcePath;
//		}
//		if(!resourcePath.endsWith("/")){
//			resourcePath = resourcePath + "/";
//		}
//		
//		String resource = config.getServletContext().getInitParameter("resourceTypes");
//		
//		if(resource == null || "".equals(resource.trim())){
//			resource = ".*\\.html;.*\\.jpg;.*\\.bmp;.*\\.png;.*\\.gif;.*\\.js;.*\\.css;.*\\.jsp";
//		}
//		String[] resources = resource.trim().split(";");
//		patterns = new Pattern[resources.length];
//		for(int n=0;n<resources.length;n++){
//			patterns[n] = Pattern.compile(resources[n]);
//		}
	}

	/**
	 * 实现对返回结果的处理，如：对返回数据压缩、数据加密等
	 * @author lin
	 * @date 2014年3月4日 上午9:23:38
	 *
	 */
//	private class HttpServletResponseImpl implements HttpServletResponse{
//
//		private HttpServletResponse response;
//
//		public HttpServletResponseImpl(HttpServletResponse response){
//			this.response = response;
//		}
//		@Override
//		public void flushBuffer() throws IOException {
//			this.response.flushBuffer();			
//		}
//
//		@Override
//		public int getBufferSize() {
//			return this.response.getBufferSize();
//		}
//
//		@Override
//		public String getCharacterEncoding() {
//			return this.response.getCharacterEncoding();
//		}
//
//		@Override
//		public String getContentType() {
//			return this.response.getContentType();
//		}
//
//		@Override
//		public Locale getLocale() {
//			return this.response.getLocale();
//		}
//
//		@Override
//		public ServletOutputStream getOutputStream() throws IOException {
//			return this.response.getOutputStream();
//		}
//
//		@Override
//		public PrintWriter getWriter() throws IOException {
//			return this.response.getWriter();
//		}
//
//		@Override
//		public boolean isCommitted() {
//			return this.response.isCommitted();
//		}
//
//		@Override
//		public void reset() {
//			this.response.reset();			
//		}
//
//		@Override
//		public void resetBuffer() {
//			this.response.resetBuffer();			
//		}
//
//		@Override
//		public void setBufferSize(int arg0) {
//			this.response.setBufferSize(arg0);			
//		}
//
//		@Override
//		public void setCharacterEncoding(String arg0) {
//			this.response.setCharacterEncoding(arg0);			
//		}
//
//		@Override
//		public void setContentLength(int arg0) {
//			this.response.setContentLength(arg0);			
//		}
//
//		@Override
//		public void setContentType(String arg0) {
//			this.response.setContentType(arg0);			
//		}
//
//		@Override
//		public void setLocale(Locale arg0) {
//			this.response.setLocale(arg0);			
//		}
//
//		@Override
//		public void addCookie(Cookie arg0) {
//			this.response.addCookie(arg0);			
//		}
//
//		@Override
//		public void addDateHeader(String arg0, long arg1) {
//			this.response.addDateHeader(arg0, arg1);			
//		}
//
//		@Override
//		public void addHeader(String arg0, String arg1) {
//			this.response.addHeader(arg0, arg1);			
//		}
//
//		@Override
//		public void addIntHeader(String arg0, int arg1) {
//			this.response.addIntHeader(arg0, arg1);
//		}
//
//		@Override
//		public boolean containsHeader(String arg0) {
//			return this.response.containsHeader(arg0);
//		}
//
//		@Override
//		public String encodeRedirectURL(String arg0) {
//			return this.response.encodeRedirectURL(arg0);
//		}
//
//		@Override
//		@Deprecated
//		public String encodeRedirectUrl(String arg0) {
//			return this.response.encodeRedirectUrl(arg0);
//		}
//
//		@Override
//		public String encodeURL(String arg0) {
//			return this.response.encodeURL(arg0);
//		}
//
//		@Override
//		@Deprecated
//		public String encodeUrl(String arg0) {
//			return this.response.encodeUrl(arg0);
//		}
//
////		@Override
////		public String getHeader(String arg0) {
////			return this.response.getHeader(arg0);
////		}
//
////		@Override
////		public Collection<String> getHeaderNames() {
////			return this.response.getHeaderNames();
////		}
//
////		@Override
////		public Collection<String> getHeaders(String arg0) {
////			return this.response.getHeaders(arg0);
////		}
//
////		@Override
////		public int getStatus() {
////			return this.response.getStatus();
////		}
//
//		@Override
//		public void sendError(int arg0) throws IOException {
//			this.response.sendError(arg0);
//		}
//
//		@Override
//		public void sendError(int arg0, String arg1) throws IOException {
//			this.response.sendError(arg0,arg1);			
//		}
//
//		@Override
//		public void sendRedirect(String arg0) throws IOException {
//			this.response.sendRedirect(arg0);			
//		}
//
//		@Override
//		public void setDateHeader(String arg0, long arg1) {
//			this.response.setDateHeader(arg0, arg1);			
//		}
//
//		@Override
//		public void setHeader(String arg0, String arg1) {
//			this.response.setHeader(arg0, arg1);			
//		}
//
//		@Override
//		public void setIntHeader(String arg0, int arg1) {
//			this.response.setIntHeader(arg0, arg1);			
//		}
//
//		@Override
//		public void setStatus(int arg0) {
//			this.response.setStatus(arg0);			
//		}
//
//		@Override
//		@Deprecated
//		public void setStatus(int arg0, String arg1) {
//			this.response.setStatus(arg0, arg1);			
//		}
//		
//	}
//	
	//改用代理对象实现，方便版本升级
	private class HttpServletRequestImpl implements HttpServletRequest{

		private HttpServletRequest request;
		private Map<String, String[]> map;

		public HttpServletRequestImpl(HttpServletRequest request,Map<String,String[]> map){
			this.request = request;
			//this.map = map;
			Map<String,String[]> tmp = new HashMap<String,String[]>();
			tmp.putAll(request.getParameterMap());
			if(map!=null){
				for(Map.Entry<String, String[]> entry:map.entrySet()){
					tmp.put(entry.getKey(), entry.getValue());
				}
			}
			this.map = tmp;
		}
		@Override
		public AsyncContext getAsyncContext() {
			return this.request.getAsyncContext();
		}

		@Override
		public Object getAttribute(String arg0) {
			return this.request.getAttribute(arg0);
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return this.request.getAttributeNames();
		}

		@Override
		public String getCharacterEncoding() {
			return this.request.getCharacterEncoding();
		}

		@Override
		public int getContentLength() {
			return this.request.getContentLength();
		}

		@Override
		public String getContentType() {
			return this.request.getContentType();
		}

		@Override
		public DispatcherType getDispatcherType() {
			return this.request.getDispatcherType();
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return this.request.getInputStream();
		}

		@Override
		public String getLocalAddr() {
			return this.request.getLocalAddr();
		}

		@Override
		public String getLocalName() {
			return this.request.getLocalName();
		}

		@Override
		public int getLocalPort() {
			return this.request.getLocalPort();
		}

		@Override
		public Locale getLocale() {
			return this.request.getLocale();
		}

		@Override
		public Enumeration<Locale> getLocales() {
			return this.request.getLocales();
		}

		@Override
		public String getParameter(String arg0) {
			//return this.request.getParameter(arg0);
			String[] vs = this.map.get(arg0);
			return (vs == null || vs.length ==0)?null:vs[0];
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			//return this.request.getParameterMap();
			return this.map;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			//return this.request.getParameterNames();
			Iterator<String> it = this.map.keySet().iterator();
			return new EnumerationString(it);
		}
		
		private class EnumerationString implements Enumeration<String>{
			
			private Iterator<String> it;

			public EnumerationString(Iterator<String> it){
				this.it = it;
			}
			@Override
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			@Override
			public String nextElement() {
				return it.next();
			}
		}

		@Override
		public String[] getParameterValues(String arg0) {
			//return this.request.getParameterValues(arg0);
			return this.map.get(arg0);
		}

		@Override
		public String getProtocol() {
			return this.request.getProtocol();
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return this.request.getReader();
		}

		@Override
		@Deprecated
		public String getRealPath(String arg0) {
			return this.request.getRealPath(arg0);
		}

		@Override
		public String getRemoteAddr() {
			return this.request.getRemoteAddr();
		}

		@Override
		public String getRemoteHost() {
			return this.request.getRemoteHost();
		}

		@Override
		public int getRemotePort() {
			return this.request.getRemotePort();
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String arg0) {
			return this.request.getRequestDispatcher(arg0);
		}

		@Override
		public String getScheme() {
			return this.request.getScheme();
		}

		@Override
		public String getServerName() {
			return this.request.getServerName();
		}

		@Override
		public int getServerPort() {
			return this.request.getServerPort();
		}

		@Override
		public ServletContext getServletContext() {
			return this.request.getServletContext();
		}

		@Override
		public boolean isAsyncStarted() {
			return this.request.isAsyncStarted();
		}

		@Override
		public boolean isAsyncSupported() {
			return this.request.isAsyncSupported();
		}

		@Override
		public boolean isSecure() {
			return this.request.isSecure();
		}

		@Override
		public void removeAttribute(String arg0) {
			this.request.removeAttribute(arg0);
		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
			this.request.setAttribute(arg0, arg1);
		}

		@Override
		public void setCharacterEncoding(String arg0)
				throws UnsupportedEncodingException {
			this.request.setCharacterEncoding(arg0);
		}

		@Override
		public AsyncContext startAsync() {
			return this.request.startAsync();
		}

		@Override
		public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
			return this.request.startAsync(arg0,arg1);
		}

		@Override
		public boolean authenticate(HttpServletResponse arg0)
				throws IOException, ServletException {
			return this.request.authenticate(arg0);
		}

		@Override
		public String getAuthType() {
			return this.request.getAuthType();
		}

		@Override
		public String getContextPath() {
			return this.request.getContextPath();
		}

		@Override
		public Cookie[] getCookies() {
			return this.request.getCookies();
		}

		@Override
		public long getDateHeader(String arg0) {
			return this.request.getDateHeader(arg0);
		}

		@Override
		public String getHeader(String arg0) {
			return this.request.getHeader(arg0);
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			return this.request.getHeaderNames();
		}

		@Override
		public Enumeration<String> getHeaders(String arg0) {
			return this.request.getHeaders(arg0);
		}

		@Override
		public int getIntHeader(String arg0) {
			return this.request.getIntHeader(arg0);
		}

		@Override
		public String getMethod() {
			return this.request.getMethod();
		}

		@Override
		public Part getPart(String arg0) throws IOException,
				IllegalStateException, ServletException {
			return this.request.getPart(arg0);
		}

		@Override
		public Collection<Part> getParts() throws IOException,
				IllegalStateException, ServletException {
			return this.request.getParts();
		}

		@Override
		public String getPathInfo() {
			return this.request.getPathInfo();
		}

		@Override
		public String getPathTranslated() {
			return this.request.getPathTranslated();
		}

		@Override
		public String getQueryString() {
			return this.request.getQueryString();
		}

		@Override
		public String getRemoteUser() {
			return this.request.getRemoteUser();
		}

		@Override
		public String getRequestURI() {
			return this.request.getRequestURI();
		}

		@Override
		public StringBuffer getRequestURL() {
			return this.request.getRequestURL();
		}

		@Override
		public String getRequestedSessionId() {
			return this.request.getRequestedSessionId();
		}

		@Override
		public String getServletPath() {
			return this.request.getServletPath();
		}

		@Override
		public HttpSession getSession() {
			return this.request.getSession();
		}

		@Override
		public HttpSession getSession(boolean arg0) {
			return this.request.getSession(arg0);
		}

		@Override
		public Principal getUserPrincipal() {
			return this.request.getUserPrincipal();
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			return this.request.isRequestedSessionIdFromCookie();
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			return this.request.isRequestedSessionIdFromURL();
		}

		@Override
		@Deprecated
		public boolean isRequestedSessionIdFromUrl() {
			return this.request.isRequestedSessionIdFromUrl();
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			return this.request.isRequestedSessionIdValid();
		}

		@Override
		public boolean isUserInRole(String arg0) {
			return this.request.isUserInRole(arg0);
		}

		@Override
		public void login(String arg0, String arg1) throws ServletException {
			this.request.login(arg0, arg1);			
		}

		@Override
		public void logout() throws ServletException {
			this.request.logout();
		}
		@Override
		public long getContentLengthLong() {
			return 0;
		}
		@Override
		public String changeSessionId() {
			return null;
		}
		@Override
		public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
				throws IOException, ServletException {
			return null;
		}
		
	}
}
