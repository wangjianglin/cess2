//package lin.web;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.reflect.Constructor;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.PeriodicEventListener;
//import org.apache.jasper.EmbeddedServletOptions;
//import org.apache.jasper.Options;
//import org.apache.jasper.compiler.JspRuntimeContext;
//import org.apache.jasper.compiler.Localizer;
//import org.apache.jasper.security.SecurityUtil;
//import org.apache.jasper.servlet.JspServletWrapper;
//
//public class LinJspServlet extends HttpServlet implements PeriodicEventListener {
//	// private Log log = LogFactory.getLog(JspServlet.class);
//	private ServletContext context;
//	private ServletConfig config;
//	private Options options;
//	private JspRuntimeContext rctxt;
//
//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//		this.config = config;
//		this.context = config.getServletContext();
//
//		String engineOptionsName = config
//				.getInitParameter("engineOptionsClass");
//
//		if (engineOptionsName != null) {
//			try {
//				ClassLoader loader = Thread.currentThread()
//						.getContextClassLoader();
//
//				Class engineOptionsClass = loader.loadClass(engineOptionsName);
//				Class[] ctorSig = { ServletConfig.class, ServletContext.class };
//				Constructor ctor = engineOptionsClass.getConstructor(ctorSig);
//				Object[] args = { config, this.context };
//				this.options = ((Options) ctor.newInstance(args));
//			} catch (Throwable e) {
//				// this.log.warn("Failed to load engineOptionsClass", e);
//
//				this.options = new EmbeddedServletOptions(config, this.context);
//			}
//		} else {
//			this.options = new EmbeddedServletOptions(config, this.context);
//		}
//		this.rctxt = new JspRuntimeContext(this.context, this.options);
//
//		// if (this.log.isDebugEnabled()) {
//		// this.log.debug(Localizer.getMessage("jsp.message.scratch.dir.is",
//		// this.options.getScratchDir().toString()));
//		//
//		// this.log.debug(Localizer.getMessage("jsp.message.dont.modify.servlets"));
//		// }
//	}
//
//	public int getJspCount() {
//		return this.rctxt.getJspCount();
//	}
//
//	public void setJspReloadCount(int count) {
//		this.rctxt.setJspReloadCount(count);
//	}
//
//	public int getJspReloadCount() {
//		return this.rctxt.getJspReloadCount();
//	}
//
//	boolean preCompile(HttpServletRequest request) throws ServletException {
//		String queryString = request.getQueryString();
//		if (queryString == null) {
//			return false;
//		}
//		int start = queryString.indexOf(Constants.PRECOMPILE);
//		if (start < 0) {
//			return false;
//		}
//		queryString = queryString.substring(start
//				+ Constants.PRECOMPILE.length());
//
//		if (queryString.length() == 0) {
//			return true;
//		}
//		if (queryString.startsWith("&")) {
//			return true;
//		}
//		if (!queryString.startsWith("=")) {
//			return false;
//		}
//		int limit = queryString.length();
//		int ampersand = queryString.indexOf("&");
//		if (ampersand > 0) {
//			limit = ampersand;
//		}
//		String value = queryString.substring(1, limit);
//		if (value.equals("true"))
//			return true;
//		if (value.equals("false")) {
//			return true;
//		}
//		throw new ServletException("Cannot have request parameter "
//				+ Constants.PRECOMPILE + " set to " + value);
//	}
//
//	public void service(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		String jspUri = null;
//
//		String jspFile = (String) request.getAttribute(Constants.JSP_FILE);
//		if (jspFile != null) {
//			jspUri = jspFile;
//		} else {
//			jspUri = (String) request
//					.getAttribute("javax.servlet.include.servlet_path");
//			if (jspUri != null) {
//				String pathInfo = (String) request
//						.getAttribute("javax.servlet.include.path_info");
//
//				if (pathInfo != null) {
//					jspUri = jspUri + pathInfo;
//				}
//
//			} else {
//				jspUri = request.getServletPath();
//				String pathInfo = request.getPathInfo();
//				if (pathInfo != null) {
//					jspUri = jspUri + pathInfo;
//				}
//			}
//		}
//		//
//		// if (this.log.isDebugEnabled()) {
//		// this.log.debug("JspEngine --> " + jspUri);
//		// this.log.debug("\t     ServletPath: " + request.getServletPath());
//		// this.log.debug("\t        PathInfo: " + request.getPathInfo());
//		// this.log.debug("\t        RealPath: " +
//		// this.context.getRealPath(jspUri));
//		// this.log.debug("\t      RequestURI: " + request.getRequestURI());
//		// this.log.debug("\t     QueryString: " + request.getQueryString());
//		// }
//		try {
//			boolean precompile = preCompile(request);
//			serviceJspFile(request, response, jspUri, null, precompile);
//		} catch (RuntimeException e) {
//			throw e;
//		} catch (ServletException e) {
//			throw e;
//		} catch (IOException e) {
//			throw e;
//		} catch (Throwable e) {
//			throw new ServletException(e);
//		}
//	}
//
//	public void destroy() {
//		// if (this.log.isDebugEnabled()) {
//		// this.log.debug("JspServlet.destroy()");
//		// }
//
//		this.rctxt.destroy();
//	}
//
//	public void periodicEvent() {
//		this.rctxt.checkCompile();
//	}
//
//	private void serviceJspFile(HttpServletRequest request,
//			HttpServletResponse response, String jspUri, Throwable exception,
//			boolean precompile) throws ServletException, IOException {
//		JspServletWrapper wrapper = this.rctxt.getWrapper(jspUri);
//		if (wrapper == null) {
//			synchronized (this) {
//				wrapper = this.rctxt.getWrapper(jspUri);
//				if (wrapper == null) {
//					if (null == this.context.getResource(jspUri)) {
//						handleMissingResource(request, response, jspUri);
//						return;
//					}
//					boolean isErrorPage = exception != null;
//					wrapper = new JspServletWrapper(this.config, this.options,
//							jspUri, isErrorPage, this.rctxt);
//
//					this.rctxt.addWrapper(jspUri, wrapper);
//				}
//			}
//		}
//		try {
//			wrapper.service(request, response, precompile);
//		} catch (FileNotFoundException fnfe) {
//			handleMissingResource(request, response, jspUri);
//		}
//	}
//
//	private void handleMissingResource(HttpServletRequest request,
//			HttpServletResponse response, String jspUri)
//			throws ServletException, IOException {
//		String includeRequestUri = (String) request
//				.getAttribute("javax.servlet.include.request_uri");
//
//		if (includeRequestUri != null) {
//			String msg = Localizer.getMessage("jsp.error.file.not.found",
//					jspUri);
//
//			throw new ServletException(SecurityUtil.filter(msg));
//		}
//		try {
//			response.sendError(404, request.getRequestURI());
//		} catch (IllegalStateException ise) {
//			// this.log.error(Localizer.getMessage("jsp.error.file.not.found",
//			// jspUri));
//		}
//	}
//}