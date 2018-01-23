package io.cess.core.spring;

import java.util.ArrayList;
import java.util.List;

import io.cess.CessException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author lin
 * @date 2015年1月27日 上午12:43:31
 *
 */
public class CessMappingHandlerAdapter extends RequestMappingHandlerAdapter{
	
	private List<HandlerMethodReturnValueHandler> customFirstReturnValueHandlers;
	private List<HttpMessageConverter<?>> customMessageConverters;
	
	@Override
	public void afterPropertiesSet() {
		if(customMessageConverters != null){
			List<HttpMessageConverter<?>> list = new ArrayList<>();

			List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();

			list.addAll(customMessageConverters);
			list.addAll(messageConverters);

			super.setMessageConverters(list);
		}
		super.afterPropertiesSet();
		if(customFirstReturnValueHandlers != null){
			List<HandlerMethodReturnValueHandler> list = new ArrayList<>();
			list.addAll(this.customFirstReturnValueHandlers);
			list.addAll(this.getReturnValueHandlers());
			
			this.setReturnValueHandlers(list);
		}
	}

	public List<HandlerMethodReturnValueHandler> getCustomFirstReturnValueHandlers() {
		return customFirstReturnValueHandlers;
	}

	public void setCustomFirstReturnValueHandlers(List<HandlerMethodReturnValueHandler> customFirstReturnValueHandlers) {
		this.customFirstReturnValueHandlers = customFirstReturnValueHandlers;
	}

	public List<HttpMessageConverter<?>> getCustomMessageConverters() {
		return customMessageConverters;
	}

	public void setCustomMessageConverters(List<HttpMessageConverter<?>> customMessageConverters) {
		this.customMessageConverters = customMessageConverters;
	}

	@Override
	protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
		CessException.claer();
		CessContext.set();
		CessContext.setHandlerMethod(handlerMethod);
		return super.invokeHandlerMethod(request, response, handlerMethod);
	}
}
//public class LinRequestMappingHandlerAdapter extends
//		AbstractHandlerMethodAdapter implements BeanFactoryAware,
//		InitializingBean {
////	public void setOrder(int order) {
////	}
////
////	@Override
////	public int getOrder() {
////		return Ordered.HIGHEST_PRECEDENCE;
////	}
//	private List<HandlerMethodArgumentResolver> customArgumentResolvers;
//
//	private HandlerMethodArgumentResolverComposite argumentResolvers;
//
//	private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;
//
//	private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
//
//	private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
//
//	private List<ModelAndViewResolver> modelAndViewResolvers;
//
//	private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
//
//	private List<HttpMessageConverter<?>> messageConverters;
//
//	private List<Object> responseBodyAdvice = new ArrayList<Object>();
//
//	private WebBindingInitializer webBindingInitializer;
//
//	private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(
//			"MvcAsync");
//
//	private Long asyncRequestTimeout;
//
//	private CallableProcessingInterceptor[] callableInterceptors = new CallableProcessingInterceptor[0];
//
//	private DeferredResultProcessingInterceptor[] deferredResultInterceptors = new DeferredResultProcessingInterceptor[0];
//
//	private boolean ignoreDefaultModelOnRedirect = false;
//
//	private int cacheSecondsForSessionAttributeHandlers = 0;
//
//	private boolean synchronizeOnSession = false;
//
//	private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
//
//	private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
//
//	private ConfigurableBeanFactory beanFactory;
//
//	private final Map<Class<?>, SessionAttributesHandler> sessionAttributesHandlerCache = new ConcurrentHashMap<Class<?>, SessionAttributesHandler>(
//			64);
//
//	private final Map<Class<?>, Set<Method>> initBinderCache = new ConcurrentHashMap<Class<?>, Set<Method>>(
//			64);
//
//	private final Map<ControllerAdviceBean, Set<Method>> initBinderAdviceCache = new LinkedHashMap<ControllerAdviceBean, Set<Method>>();
//
//	private final Map<Class<?>, Set<Method>> modelAttributeCache = new ConcurrentHashMap<Class<?>, Set<Method>>(
//			64);
//
//	private final Map<ControllerAdviceBean, Set<Method>> modelAttributeAdviceCache = new LinkedHashMap<ControllerAdviceBean, Set<Method>>();
//
//	public LinRequestMappingHandlerAdapter() {
//		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
//		stringHttpMessageConverter.setWriteAcceptCharset(false); // see SPR-7316
//
//		this.messageConverters = new ArrayList<HttpMessageConverter<?>>(4);
//		this.messageConverters.add(new ByteArrayHttpMessageConverter());
//		this.messageConverters.add(stringHttpMessageConverter);
//		this.messageConverters.add(new SourceHttpMessageConverter<Source>());
//		this.messageConverters
//				.add(new AllEncompassingFormHttpMessageConverter());
//	}
//
//	/**
//	 * Provide resolvers for custom argument types. Custom resolvers are ordered
//	 * after built-in ones. To override the built-in support for argument
//	 * resolution use {@link #setArgumentResolvers} instead.
//	 */
//	public void setCustomArgumentResolvers(
//			List<HandlerMethodArgumentResolver> argumentResolvers) {
//		this.customArgumentResolvers = argumentResolvers;
//	}
//
//	/**
//	 * Return the custom argument resolvers, or {@code null}.
//	 */
//	public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
//		return this.customArgumentResolvers;
//	}
//
//	/**
//	 * Configure the complete list of supported argument types thus overriding
//	 * the resolvers that would otherwise be configured by default.
//	 */
//	public void setArgumentResolvers(
//			List<HandlerMethodArgumentResolver> argumentResolvers) {
//		if (argumentResolvers == null) {
//			this.argumentResolvers = null;
//		} else {
//			this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
//			this.argumentResolvers.addResolvers(argumentResolvers);
//		}
//	}
//
//	/**
//	 * Return the configured argument resolvers, or possibly {@code null} if not
//	 * initialized yet via {@link #afterPropertiesSet()}.
//	 */
//	public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
//		return (this.argumentResolvers != null) ? this.argumentResolvers
//				.getResolvers() : null;
//	}
//
//	/**
//	 * Configure the supported argument types in {@code @InitBinder} methods.
//	 */
//	public void setInitBinderArgumentResolvers(
//			List<HandlerMethodArgumentResolver> argumentResolvers) {
//		if (argumentResolvers == null) {
//			this.initBinderArgumentResolvers = null;
//		} else {
//			this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
//			this.initBinderArgumentResolvers.addResolvers(argumentResolvers);
//		}
//	}
//
//	/**
//	 * Return the argument resolvers for {@code @InitBinder} methods, or
//	 * possibly {@code null} if not initialized yet via
//	 * {@link #afterPropertiesSet()}.
//	 */
//	public List<HandlerMethodArgumentResolver> getInitBinderArgumentResolvers() {
//		return (this.initBinderArgumentResolvers != null) ? this.initBinderArgumentResolvers
//				.getResolvers() : null;
//	}
//
//	/**
//	 * Provide handlers for custom return value types. Custom handlers are
//	 * ordered after built-in ones. To override the built-in support for return
//	 * value handling use {@link #setReturnValueHandlers}.
//	 */
//	public void setCustomReturnValueHandlers(
//			List<HandlerMethodReturnValueHandler> returnValueHandlers) {
//		this.customReturnValueHandlers = returnValueHandlers;
//	}
//
//	/**
//	 * Return the custom return value handlers, or {@code null}.
//	 */
//	public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
//		return this.customReturnValueHandlers;
//	}
//
//	/**
//	 * Configure the complete list of supported return value types thus
//	 * overriding handlers that would otherwise be configured by default.
//	 */
//	public void setReturnValueHandlers(
//			List<HandlerMethodReturnValueHandler> returnValueHandlers) {
//		if (returnValueHandlers == null) {
//			this.returnValueHandlers = null;
//		} else {
//			this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
//			this.returnValueHandlers.addHandlers(returnValueHandlers);
//		}
//	}
//
//	/**
//	 * Return the configured handlers, or possibly {@code null} if not
//	 * initialized yet via {@link #afterPropertiesSet()}.
//	 */
//	public List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
//		return this.returnValueHandlers.getHandlers();
//	}
//
//	/**
//	 * Provide custom {@link ModelAndViewResolver}s.
//	 * <p>
//	 * <strong>Note:</strong> This method is available for backwards
//	 * compatibility only. However, it is recommended to re-write a
//	 * {@code ModelAndViewResolver} as {@link HandlerMethodReturnValueHandler}.
//	 * An adapter between the two interfaces is not possible since the
//	 * {@link HandlerMethodReturnValueHandler#supportsReturnType} method cannot
//	 * be implemented. Hence {@code ModelAndViewResolver}s are limited to always
//	 * being invoked at the end after all other return value handlers have been
//	 * given a chance.
//	 * <p>
//	 * A {@code HandlerMethodReturnValueHandler} provides better access to the
//	 * return type and controller method information and can be ordered freely
//	 * relative to other return value handlers.
//	 */
//	public void setModelAndViewResolvers(
//			List<ModelAndViewResolver> modelAndViewResolvers) {
//		this.modelAndViewResolvers = modelAndViewResolvers;
//	}
//
//	/**
//	 * Return the configured {@link ModelAndViewResolver}s, or {@code null}.
//	 */
//	public List<ModelAndViewResolver> getModelAndViewResolvers() {
//		return modelAndViewResolvers;
//	}
//
//	/**
//	 * Set the {@link ContentNegotiationManager} to use to determine requested
//	 * media types. If not set, the default constructor is used.
//	 */
//	public void setContentNegotiationManager(
//			ContentNegotiationManager contentNegotiationManager) {
//		this.contentNegotiationManager = contentNegotiationManager;
//	}
//
//	/**
//	 * Provide the converters to use in argument resolvers and return value
//	 * handlers that support reading and/or writing to the body of the request
//	 * and response.
//	 */
//	public void setMessageConverters(
//			List<HttpMessageConverter<?>> messageConverters) {
//		this.messageConverters = messageConverters;
//	}
//
//	/**
//	 * Return the configured message body converters.
//	 */
//	public List<HttpMessageConverter<?>> getMessageConverters() {
//		return this.messageConverters;
//	}
//
//	/**
//	 * Add one or more components to modify the response after the execution of
//	 * a controller method annotated with {@code @ResponseBody}, or a method
//	 * returning {@code ResponseEntity} and before the body is written to the
//	 * response with the selected {@code HttpMessageConverter}.
//	 */
//	public void setResponseBodyAdvice(
//			List<ResponseBodyAdvice<?>> responseBodyAdvice) {
//		this.responseBodyAdvice.clear();
//		if (responseBodyAdvice != null) {
//			this.responseBodyAdvice.addAll(responseBodyAdvice);
//		}
//	}
//
//	/**
//	 * Provide a WebBindingInitializer with "global" initialization to apply to
//	 * every DataBinder instance.
//	 */
//	public void setWebBindingInitializer(
//			WebBindingInitializer webBindingInitializer) {
//		this.webBindingInitializer = webBindingInitializer;
//	}
//
//	/**
//	 * Return the configured WebBindingInitializer, or {@code null} if none.
//	 */
//	public WebBindingInitializer getWebBindingInitializer() {
//		return this.webBindingInitializer;
//	}
//
//	/**
//	 * Set the default {@link AsyncTaskExecutor} to use when a controller method
//	 * return a {@link Callable}. Controller methods can override this default
//	 * on a per-request basis by returning an {@link WebAsyncTask}.
//	 * <p>
//	 * By default a {@link SimpleAsyncTaskExecutor} instance is used. It's
//	 * recommended to change that default in production as the simple executor
//	 * does not re-use threads.
//	 */
//	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
//		this.taskExecutor = taskExecutor;
//	}
//
//	/**
//	 * Specify the amount of time, in milliseconds, before concurrent handling
//	 * should time out. In Servlet 3, the timeout begins after the main request
//	 * processing thread has exited and ends when the request is dispatched
//	 * again for further processing of the concurrently produced result.
//	 * <p>
//	 * If this value is not set, the default timeout of the underlying
//	 * implementation is used, e.g. 10 seconds on Tomcat with Servlet 3.
//	 * 
//	 * @param timeout
//	 *            the timeout value in milliseconds
//	 */
//	public void setAsyncRequestTimeout(long timeout) {
//		this.asyncRequestTimeout = timeout;
//	}
//
//	/**
//	 * Configure {@code CallableProcessingInterceptor}'s to register on async
//	 * requests.
//	 * 
//	 * @param interceptors
//	 *            the interceptors to register
//	 */
//	public void setCallableInterceptors(
//			List<CallableProcessingInterceptor> interceptors) {
//		Assert.notNull(interceptors);
//		this.callableInterceptors = interceptors
//				.toArray(new CallableProcessingInterceptor[interceptors.size()]);
//	}
//
//	/**
//	 * Configure {@code DeferredResultProcessingInterceptor}'s to register on
//	 * async requests.
//	 * 
//	 * @param interceptors
//	 *            the interceptors to register
//	 */
//	public void setDeferredResultInterceptors(
//			List<DeferredResultProcessingInterceptor> interceptors) {
//		Assert.notNull(interceptors);
//		this.deferredResultInterceptors = interceptors
//				.toArray(new DeferredResultProcessingInterceptor[interceptors
//						.size()]);
//	}
//
//	/**
//	 * By default the content of the "default" model is used both during
//	 * rendering and redirect scenarios. Alternatively a controller method can
//	 * declare a {@link RedirectAttributes} argument and use it to provide
//	 * attributes for a redirect.
//	 * <p>
//	 * Setting this flag to {@code true} guarantees the "default" model is never
//	 * used in a redirect scenario even if a RedirectAttributes argument is not
//	 * declared. Setting it to {@code false} means the "default" model may be
//	 * used in a redirect if the controller method doesn't declare a
//	 * RedirectAttributes argument.
//	 * <p>
//	 * The default setting is {@code false} but new applications should consider
//	 * setting it to {@code true}.
//	 * 
//	 * @see RedirectAttributes
//	 */
//	public void setIgnoreDefaultModelOnRedirect(
//			boolean ignoreDefaultModelOnRedirect) {
//		this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
//	}
//
//	/**
//	 * Specify the strategy to store session attributes with. The default is
//	 * {@link org.springframework.web.bind.support.DefaultSessionAttributeStore}
//	 * , storing session attributes in the HttpSession with the same attribute
//	 * name as in the model.
//	 */
//	public void setSessionAttributeStore(
//			SessionAttributeStore sessionAttributeStore) {
//		this.sessionAttributeStore = sessionAttributeStore;
//	}
//
//	/**
//	 * Cache content produced by {@code @SessionAttributes} annotated handlers
//	 * for the given number of seconds. Default is 0, preventing caching
//	 * completely.
//	 * <p>
//	 * In contrast to the "cacheSeconds" property which will apply to all
//	 * general handlers (but not to {@code @SessionAttributes} annotated
//	 * handlers), this setting will apply to {@code @SessionAttributes} handlers
//	 * only.
//	 * 
//	 * @see #setCacheSeconds
//	 * @see org.springframework.web.bind.annotation.SessionAttributes
//	 */
//	public void setCacheSecondsForSessionAttributeHandlers(
//			int cacheSecondsForSessionAttributeHandlers) {
//		this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
//	}
//
//	/**
//	 * Set if controller execution should be synchronized on the session, to
//	 * serialize parallel invocations from the same client.
//	 * <p>
//	 * More specifically, the execution of the {@code handleRequestInternal}
//	 * method will get synchronized if this flag is "true". The best available
//	 * session mutex will be used for the synchronization; ideally, this will be
//	 * a mutex exposed by HttpSessionMutexListener.
//	 * <p>
//	 * The session mutex is guaranteed to be the same object during the entire
//	 * lifetime of the session, available under the key defined by the
//	 * {@code SESSION_MUTEX_ATTRIBUTE} constant. It serves as a safe reference
//	 * to synchronize on for locking on the current session.
//	 * <p>
//	 * In many cases, the HttpSession reference itself is a safe mutex as well,
//	 * since it will always be the same object reference for the same active
//	 * logical session. However, this is not guaranteed across different servlet
//	 * containers; the only 100% safe way is a session mutex.
//	 * 
//	 * @see org.springframework.web.util.HttpSessionMutexListener
//	 * @see org.springframework.web.util.WebUtils#getSessionMutex(javax.servlet.http1.HttpSession)
//	 */
//	public void setSynchronizeOnSession(boolean synchronizeOnSession) {
//		this.synchronizeOnSession = synchronizeOnSession;
//	}
//
//	/**
//	 * Set the ParameterNameDiscoverer to use for resolving method parameter
//	 * names if needed (e.g. for default attribute names).
//	 * <p>
//	 * Default is a
//	 * {@link org.springframework.core.DefaultParameterNameDiscoverer}.
//	 */
//	public void setParameterNameDiscoverer(
//			ParameterNameDiscoverer parameterNameDiscoverer) {
//		this.parameterNameDiscoverer = parameterNameDiscoverer;
//	}
//
//	/**
//	 * A {@link ConfigurableBeanFactory} is expected for resolving expressions
//	 * in method argument default values.
//	 */
//	@Override
//	public void setBeanFactory(BeanFactory beanFactory) {
//		if (beanFactory instanceof ConfigurableBeanFactory) {
//			this.beanFactory = (ConfigurableBeanFactory) beanFactory;
//		}
//	}
//
//	/**
//	 * Return the owning factory of this bean instance, or {@code null} if none.
//	 */
//	protected ConfigurableBeanFactory getBeanFactory() {
//		return this.beanFactory;
//	}
//
//	@Override
//	public void afterPropertiesSet() {
//		// Do this first, it may add ResponseBody advice beans
//		initControllerAdviceCache();
//
//		if (this.argumentResolvers == null) {
//			List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
//			this.argumentResolvers = new HandlerMethodArgumentResolverComposite()
//					.addResolvers(resolvers);
//		}
//		if (this.initBinderArgumentResolvers == null) {
//			List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
//			this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite()
//					.addResolvers(resolvers);
//		}
//		if (this.returnValueHandlers == null) {
//			List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
//			this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite()
//					.addHandlers(handlers);
//		}
//	}
//
//	private void initControllerAdviceCache() {
//		if (getApplicationContext() == null) {
//			return;
//		}
//		if (logger.isInfoEnabled()) {
//			logger.info("Looking for @ControllerAdvice: "
//					+ getApplicationContext());
//		}
//
//		List<ControllerAdviceBean> beans = ControllerAdviceBean
//				.findAnnotatedBeans(getApplicationContext());
//		Collections.sort(beans, new OrderComparator());
//
//		List<Object> responseBodyAdviceBeans = new ArrayList<Object>();
//
//		for (ControllerAdviceBean bean : beans) {
//			Set<Method> attrMethods = HandlerMethodSelector.selectMethods(
//					bean.getBeanType(), MODEL_ATTRIBUTE_METHODS);
//			if (!attrMethods.isEmpty()) {
//				this.modelAttributeAdviceCache.put(bean, attrMethods);
//				logger.info("Detected @ModelAttribute methods in " + bean);
//			}
//			Set<Method> binderMethods = HandlerMethodSelector.selectMethods(
//					bean.getBeanType(), INIT_BINDER_METHODS);
//			if (!binderMethods.isEmpty()) {
//				this.initBinderAdviceCache.put(bean, binderMethods);
//				logger.info("Detected @InitBinder methods in " + bean);
//			}
//			if (ResponseBodyAdvice.class.isAssignableFrom(bean.getBeanType())) {
//				responseBodyAdviceBeans.add(bean);
//				logger.info("Detected ResponseBodyAdvice bean in " + bean);
//			}
//		}
//
//		if (!responseBodyAdviceBeans.isEmpty()) {
//			this.responseBodyAdvice.addAll(0, responseBodyAdviceBeans);
//		}
//	}
//
//	/**
//	 * Return the list of argument resolvers to use including built-in resolvers
//	 * and custom resolvers provided via {@link #setCustomArgumentResolvers}.
//	 */
//	private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
//		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();
//
//		// Annotation-based argument resolution
//		resolvers.add(new HandlerMethodArgumentResolverComposite());
////		resolvers.add(new LinServletModelAttributeMethodProcessor(false));
//		resolvers.add(new LinServletModelAttributeMethodProcessor());
//		
//		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
//				false));
//		resolvers.add(new RequestParamMapMethodArgumentResolver());
//		resolvers.add(new PathVariableMethodArgumentResolver());
//		resolvers.add(new PathVariableMapMethodArgumentResolver());
//		resolvers.add(new MatrixVariableMethodArgumentResolver());
//		resolvers.add(new MatrixVariableMapMethodArgumentResolver());
//		resolvers.add(new ServletModelAttributeMethodProcessor(false));
//		resolvers.add(new LinRequestResponseBodyMethodProcessor(
//				getMessageConverters()));
//		resolvers.add(new RequestPartMethodArgumentResolver(
//				getMessageConverters()));
//		resolvers
//				.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
//		resolvers.add(new RequestHeaderMapMethodArgumentResolver());
//		resolvers.add(new ServletCookieValueMethodArgumentResolver(
//				getBeanFactory()));
//		resolvers.add(new ExpressionValueMethodArgumentResolver(
//				getBeanFactory()));
//
//		// Type-based argument resolution
//		resolvers.add(new ServletRequestMethodArgumentResolver());
//		resolvers.add(new ServletResponseMethodArgumentResolver());
//		resolvers.add(new HttpEntityMethodProcessor(getMessageConverters()));
//		resolvers.add(new RedirectAttributesMethodArgumentResolver());
//		resolvers.add(new ModelMethodProcessor());
//		resolvers.add(new MapMethodProcessor());
//		resolvers.add(new ErrorsMethodArgumentResolver());
//		resolvers.add(new SessionStatusMethodArgumentResolver());
//		resolvers.add(new UriComponentsBuilderMethodArgumentResolver());
//
//		// Custom arguments
//		if (getCustomArgumentResolvers() != null) {
//			resolvers.addAll(getCustomArgumentResolvers());
//		}
//
//		// Catch-all
//		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
//				true));
////		resolvers.add(new LinServletModelAttributeMethodProcessor(true));
//
//		return resolvers;
//	}
//
//	/**
//	 * Return the list of argument resolvers to use for {@code @InitBinder}
//	 * methods including built-in and custom resolvers.
//	 */
//	private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers() {
//		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();
//
//		// Annotation-based argument resolution
//		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
//				false));
//		resolvers.add(new RequestParamMapMethodArgumentResolver());
//		resolvers.add(new PathVariableMethodArgumentResolver());
//		resolvers.add(new PathVariableMapMethodArgumentResolver());
//		resolvers.add(new MatrixVariableMethodArgumentResolver());
//		resolvers.add(new MatrixVariableMapMethodArgumentResolver());
//		resolvers.add(new ExpressionValueMethodArgumentResolver(
//				getBeanFactory()));
//
//		// Type-based argument resolution
//		resolvers.add(new ServletRequestMethodArgumentResolver());
//		resolvers.add(new ServletResponseMethodArgumentResolver());
//
//		// Custom arguments
//		if (getCustomArgumentResolvers() != null) {
//			resolvers.addAll(getCustomArgumentResolvers());
//		}
//
//		// Catch-all
//		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
//				true));
//
//		return resolvers;
//	}
//
//	/**
//	 * Return the list of return value handlers to use including built-in and
//	 * custom handlers provided via {@link #setReturnValueHandlers}.
//	 */
//	private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
//		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>();
//
//		// Single-purpose return value types
//		handlers.add(new ModelAndViewMethodReturnValueHandler());
//		handlers.add(new ModelMethodProcessor());
//		handlers.add(new ViewMethodReturnValueHandler());
//		handlers.add(new HttpEntityMethodProcessor(getMessageConverters(),
//				this.contentNegotiationManager, this.responseBodyAdvice));
//		handlers.add(new HttpHeadersReturnValueHandler());
//		handlers.add(new CallableMethodReturnValueHandler());
//		handlers.add(new DeferredResultMethodReturnValueHandler());
//		handlers.add(new AsyncTaskMethodReturnValueHandler(this.beanFactory));
//		handlers.add(new ListenableFutureReturnValueHandler());
//
//		// Annotation-based return value types
//		handlers.add(new ModelAttributeMethodProcessor(false));
//		handlers.add(new LinRequestResponseBodyMethodProcessor(
//				getMessageConverters(), this.contentNegotiationManager,
//				this.responseBodyAdvice));
//
//		// Multi-purpose return value types
//		handlers.add(new ViewNameMethodReturnValueHandler());
//		handlers.add(new MapMethodProcessor());
//
//		// Custom return value types
//		if (getCustomReturnValueHandlers() != null) {
//			handlers.addAll(getCustomReturnValueHandlers());
//		}
//
//		// Catch-all
//		if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
//			handlers.add(new ModelAndViewResolverMethodReturnValueHandler(
//					getModelAndViewResolvers()));
//		} else {
//			handlers.add(new ModelAttributeMethodProcessor(true));
//		}
//
//		return handlers;
//	}
//
//	/**
//	 * Always return {@code true} since any method argument and return value
//	 * type will be processed in some way. A method argument not recognized by
//	 * any HandlerMethodArgumentResolver is interpreted as a request parameter
//	 * if it is a simple type, or as a model attribute otherwise. A return value
//	 * not recognized by any HandlerMethodReturnValueHandler will be interpreted
//	 * as a model attribute.
//	 */
//	@Override
//	protected boolean supportsInternal(HandlerMethod handlerMethod) {
//		return true;
//	}
//
//	@Override
//	protected ModelAndView handleInternal(HttpServletRequest request,
//			HttpServletResponse response, HandlerMethod handlerMethod)
//			throws Exception {
//
//		if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
//			// Always prevent caching in case of session attribute management.
//			checkAndPrepare(request, response,
//					this.cacheSecondsForSessionAttributeHandlers, true);
//		} else {
//			// Uses configured default cacheSeconds setting.
//			checkAndPrepare(request, response, true);
//		}
//
//		// Execute invokeHandlerMethod in synchronized block if required.
//		if (this.synchronizeOnSession) {
//			HttpSession session = request.getSession(false);
//			if (session != null) {
//				Object mutex = WebUtils.getSessionMutex(session);
//				synchronized (mutex) {
//					return invokeHandleMethod(request, response, handlerMethod);
//				}
//			}
//		}
//
//		return invokeHandleMethod(request, response, handlerMethod);
//	}
//
//	/**
//	 * This implementation always returns -1. An {@code @RequestMapping} method
//	 * can calculate the lastModified value, call
//	 * {@link WebRequest#checkNotModified(long)}, and return {@code null} if the
//	 * result of that call is {@code true}.
//	 */
//	@Override
//	protected long getLastModifiedInternal(HttpServletRequest request,
//			HandlerMethod handlerMethod) {
//		return -1;
//	}
//
//	/**
//	 * Return the {@link SessionAttributesHandler} instance for the given
//	 * handler type (never {@code null}).
//	 */
//	private SessionAttributesHandler getSessionAttributesHandler(
//			HandlerMethod handlerMethod) {
//		Class<?> handlerType = handlerMethod.getBeanType();
//		SessionAttributesHandler sessionAttrHandler = this.sessionAttributesHandlerCache
//				.get(handlerType);
//		if (sessionAttrHandler == null) {
//			synchronized (this.sessionAttributesHandlerCache) {
//				sessionAttrHandler = this.sessionAttributesHandlerCache
//						.get(handlerType);
//				if (sessionAttrHandler == null) {
//					sessionAttrHandler = new SessionAttributesHandler(
//							handlerType, sessionAttributeStore);
//					this.sessionAttributesHandlerCache.put(handlerType,
//							sessionAttrHandler);
//				}
//			}
//		}
//		return sessionAttrHandler;
//	}
//
//	/**
//	 * Invoke the {@link RequestMapping} handler method preparing a
//	 * {@link ModelAndView} if view resolution is required.
//	 */
//	private ModelAndView invokeHandleMethod(HttpServletRequest request,
//			HttpServletResponse response, HandlerMethod handlerMethod)
//			throws Exception {
//
//		ServletWebRequest webRequest = new ServletWebRequest(request, response);
//
//		WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
//		ModelFactory modelFactory = getModelFactory(handlerMethod,
//				binderFactory);
//		ServletInvocableHandlerMethod requestMappingMethod = createRequestMappingMethod(
//				handlerMethod, binderFactory);
//
//		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
//		mavContainer.addAllAttributes(RequestContextUtils
//				.getInputFlashMap(request));
//		modelFactory.initModel(webRequest, mavContainer, requestMappingMethod);
//		mavContainer
//				.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
//
//		AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(
//				request, response);
//		asyncWebRequest.setTimeout(this.asyncRequestTimeout);
//
//		final WebAsyncManager asyncManager = WebAsyncUtils
//				.getAsyncManager(request);
//		asyncManager.setTaskExecutor(this.taskExecutor);
//		asyncManager.setAsyncWebRequest(asyncWebRequest);
//		asyncManager.registerCallableInterceptors(this.callableInterceptors);
//		asyncManager
//				.registerDeferredResultInterceptors(this.deferredResultInterceptors);
//
//		if (asyncManager.hasConcurrentResult()) {
//			Object result = asyncManager.getConcurrentResult();
//			mavContainer = (ModelAndViewContainer) asyncManager
//					.getConcurrentResultContext()[0];
//			asyncManager.clearConcurrentResult();
//
//			if (logger.isDebugEnabled()) {
//				logger.debug("Found concurrent result value [" + result + "]");
//			}
//			requestMappingMethod = requestMappingMethod
//					.wrapConcurrentResult(result);
//		}
//
//		requestMappingMethod.invokeAndHandle(webRequest, mavContainer);
//
//		if (asyncManager.isConcurrentHandlingStarted()) {
//			return null;
//		}
//
//		return getModelAndView(mavContainer, modelFactory, webRequest);
//	}
//
//	private ServletInvocableHandlerMethod createRequestMappingMethod(
//			HandlerMethod handlerMethod, WebDataBinderFactory binderFactory) {
//
//		ServletInvocableHandlerMethod requestMethod;
//		requestMethod = new ServletInvocableHandlerMethod(handlerMethod);
//		requestMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
//		requestMethod
//				.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
//		requestMethod.setDataBinderFactory(binderFactory);
//		requestMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
//		return requestMethod;
//	}
//
//	private ModelFactory getModelFactory(HandlerMethod handlerMethod,
//			WebDataBinderFactory binderFactory) {
//		SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
//		Class<?> handlerType = handlerMethod.getBeanType();
//		Set<Method> methods = this.modelAttributeCache.get(handlerType);
//		if (methods == null) {
//			methods = HandlerMethodSelector.selectMethods(handlerType,
//					MODEL_ATTRIBUTE_METHODS);
//			this.modelAttributeCache.put(handlerType, methods);
//		}
//		List<InvocableHandlerMethod> attrMethods = new ArrayList<InvocableHandlerMethod>();
//		// Global methods first
//		for (Entry<ControllerAdviceBean, Set<Method>> entry : this.modelAttributeAdviceCache
//				.entrySet()) {
//			if (entry.getKey().isApplicableToBeanType(handlerType)) {
//				Object bean = entry.getKey().resolveBean();
//				for (Method method : entry.getValue()) {
//					attrMethods.add(createModelAttributeMethod(binderFactory,
//							bean, method));
//				}
//			}
//		}
//		for (Method method : methods) {
//			Object bean = handlerMethod.getBean();
//			attrMethods.add(createModelAttributeMethod(binderFactory, bean,
//					method));
//		}
//		return new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
//	}
//
//	private InvocableHandlerMethod createModelAttributeMethod(
//			WebDataBinderFactory factory, Object bean, Method method) {
//		InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(bean,
//				method);
//		attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
//		attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
//		attrMethod.setDataBinderFactory(factory);
//		return attrMethod;
//	}
//
//	private WebDataBinderFactory getDataBinderFactory(
//			HandlerMethod handlerMethod) throws Exception {
//		Class<?> handlerType = handlerMethod.getBeanType();
//		Set<Method> methods = this.initBinderCache.get(handlerType);
//		if (methods == null) {
//			methods = HandlerMethodSelector.selectMethods(handlerType,
//					INIT_BINDER_METHODS);
//			this.initBinderCache.put(handlerType, methods);
//		}
//		List<InvocableHandlerMethod> initBinderMethods = new ArrayList<InvocableHandlerMethod>();
//		// Global methods first
//		for (Entry<ControllerAdviceBean, Set<Method>> entry : this.initBinderAdviceCache
//				.entrySet()) {
//			if (entry.getKey().isApplicableToBeanType(handlerType)) {
//				Object bean = entry.getKey().resolveBean();
//				for (Method method : entry.getValue()) {
//					initBinderMethods.add(createInitBinderMethod(bean, method));
//				}
//			}
//		}
//		for (Method method : methods) {
//			Object bean = handlerMethod.getBean();
//			initBinderMethods.add(createInitBinderMethod(bean, method));
//		}
//		return createDataBinderFactory(initBinderMethods);
//	}
//
//	private InvocableHandlerMethod createInitBinderMethod(Object bean,
//			Method method) {
//		InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(bean,
//				method);
//		binderMethod
//				.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
//		binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(
//				this.webBindingInitializer));
//		binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
//		return binderMethod;
//	}
//
//	/**
//	 * Template method to create a new InitBinderDataBinderFactory instance.
//	 * <p>
//	 * The default implementation creates a ServletRequestDataBinderFactory.
//	 * This can be overridden for custom ServletRequestDataBinder subclasses.
//	 * 
//	 * @param binderMethods
//	 *            {@code @InitBinder} methods
//	 * @return the InitBinderDataBinderFactory instance to use
//	 * @throws Exception
//	 *             in case of invalid state or arguments
//	 */
//	protected InitBinderDataBinderFactory createDataBinderFactory(
//			List<InvocableHandlerMethod> binderMethods) throws Exception {
//
////		return new ServletRequestDataBinderFactory(binderMethods,
////				getWebBindingInitializer());
//		return new LinServletRequestDataBinderFactory(binderMethods,
//				getWebBindingInitializer());
//	}
//
//	private ModelAndView getModelAndView(ModelAndViewContainer mavContainer,
//			ModelFactory modelFactory, NativeWebRequest webRequest)
//			throws Exception {
//
//		modelFactory.updateModel(webRequest, mavContainer);
//		if (mavContainer.isRequestHandled()) {
//			return null;
//		}
//		ModelMap model = mavContainer.getModel();
//		ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model);
//		if (!mavContainer.isViewReference()) {
//			mav.setView((View) mavContainer.getView());
//		}
//		if (model instanceof RedirectAttributes) {
//			Map<String, ?> flashAttributes = ((RedirectAttributes) model)
//					.getFlashAttributes();
//			HttpServletRequest request = webRequest
//					.getNativeRequest(HttpServletRequest.class);
//			RequestContextUtils.getOutputFlashMap(request).putAll(
//					flashAttributes);
//		}
//		return mav;
//	}
//
//	/**
//	 * MethodFilter that matches {@link InitBinder @InitBinder} methods.
//	 */
//	public static final MethodFilter INIT_BINDER_METHODS = new MethodFilter() {
//
//		@Override
//		public boolean matches(Method method) {
//			return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
//		}
//	};
//
//	/**
//	 * MethodFilter that matches {@link ModelAttribute @ModelAttribute} methods.
//	 */
//	public static final MethodFilter MODEL_ATTRIBUTE_METHODS = new MethodFilter() {
//
//		@Override
//		public boolean matches(Method method) {
//			return ((AnnotationUtils.findAnnotation(method,
//					RequestMapping.class) == null) && (AnnotationUtils
//					.findAnnotation(method, ModelAttribute.class) != null));
//		}
//	};
//
//}
//
//class ServletInvocableHandlerMethod extends InvocableHandlerMethod {
//
//	private static final Method CALLABLE_METHOD = ClassUtils.getMethod(
//			Callable.class, "call");
//
//	private HttpStatus responseStatus;
//
//	private String responseReason;
//
//	private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
//
//	/**
//	 * Creates an instance from the given handler and method.
//	 */
//	public ServletInvocableHandlerMethod(Object handler, Method method) {
//		super(handler, method);
//		initResponseStatus();
//	}
//
//	/**
//	 * Create an instance from a {@code HandlerMethod}.
//	 */
//	public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
//		super(handlerMethod);
//		initResponseStatus();
//	}
//
//	private void initResponseStatus() {
//		ResponseStatus annotation = getMethodAnnotation(ResponseStatus.class);
//		if (annotation != null) {
//			this.responseStatus = annotation.value();
//			this.responseReason = annotation.reason();
//		}
//	}
//
//	/**
//	 * Register {@link HandlerMethodReturnValueHandler} instances to use to
//	 * handle return values.
//	 */
//	public void setHandlerMethodReturnValueHandlers(
//			HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
//		this.returnValueHandlers = returnValueHandlers;
//	}
//
//	/**
//	 * Invokes the method and handles the return value through one of the
//	 * configured {@link HandlerMethodReturnValueHandler}s.
//	 * 
//	 * @param webRequest
//	 *            the current request
//	 * @param mavContainer
//	 *            the ModelAndViewContainer for this request
//	 * @param providedArgs
//	 *            "given" arguments matched by type (not resolved)
//	 */
//	public void invokeAndHandle(ServletWebRequest webRequest,
//			ModelAndViewContainer mavContainer, Object... providedArgs)
//			throws Exception {
//
//		Object returnValue = invokeForRequest(webRequest, mavContainer,
//				providedArgs);
//		setResponseStatus(webRequest);
//
//		if (returnValue == null) {
//			if (isRequestNotModified(webRequest) || hasResponseStatus()
//					|| mavContainer.isRequestHandled()) {
//				mavContainer.setRequestHandled(true);
//				return;
//			}
//		} else if (StringUtils.hasText(this.responseReason)) {
//			mavContainer.setRequestHandled(true);
//			return;
//		}
//
//		mavContainer.setRequestHandled(false);
//		try {
//			this.returnValueHandlers.handleReturnValue(returnValue,
//					getReturnValueType(returnValue), mavContainer, webRequest);
//		} catch (Exception ex) {
//			if (logger.isTraceEnabled()) {
//				logger.trace(
//						getReturnValueHandlingErrorMessage(
//								"Error handling return value", returnValue), ex);
//			}
//			throw ex;
//		}
//	}
//
//	/**
//	 * Set the response status according to the {@link ResponseStatus}
//	 * annotation.
//	 */
//	private void setResponseStatus(ServletWebRequest webRequest)
//			throws IOException {
//		if (this.responseStatus == null) {
//			return;
//		}
//		if (StringUtils.hasText(this.responseReason)) {
//			webRequest.getResponse().sendError(this.responseStatus.value(),
//					this.responseReason);
//		} else {
//			webRequest.getResponse().setStatus(this.responseStatus.value());
//		}
//		// to be picked up by the RedirectView
//		webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE,
//				this.responseStatus);
//	}
//
//	/**
//	 * Does the given request qualify as "not modified"?
//	 * 
//	 * @see ServletWebRequest#checkNotModified(long)
//	 * @see ServletWebRequest#checkNotModified(String)
//	 */
//	private boolean isRequestNotModified(ServletWebRequest webRequest) {
//		return webRequest.isNotModified();
//	}
//
//	/**
//	 * Does this method have the response status instruction?
//	 */
//	private boolean hasResponseStatus() {
//		return (this.responseStatus != null);
//	}
//
//	private String getReturnValueHandlingErrorMessage(String message,
//			Object returnValue) {
//		StringBuilder sb = new StringBuilder(message);
//		if (returnValue != null) {
//			sb.append(" [type=").append(returnValue.getClass().getName())
//					.append("]");
//		}
//		sb.append(" [value=").append(returnValue).append("]");
//		return getDetailedErrorMessage(sb.toString());
//	}
//
//	/**
//	 * Create a nested ServletInvocableHandlerMethod subclass that returns the
//	 * the given value (or raises an Exception if the value is one) rather than
//	 * actually invoking the controller method. This is useful when processing
//	 * async return values (e.g. Callable, DeferredResult, ListenableFuture).
//	 */
//	ServletInvocableHandlerMethod wrapConcurrentResult(Object result) {
//		return new ConcurrentResultHandlerMethod(result,
//				new ConcurrentResultMethodParameter(result));
//	}
//
//	/**
//	 * A nested subclass of {@code ServletInvocableHandlerMethod} that uses a
//	 * simple {@link Callable} instead of the original controller as the handler
//	 * in order to return the fixed (concurrent) result value given to it.
//	 * Effectively "resumes" processing with the asynchronously produced return
//	 * value.
//	 */
//	private class ConcurrentResultHandlerMethod extends
//			ServletInvocableHandlerMethod {
//
//		private final MethodParameter returnType;
//
//		public ConcurrentResultHandlerMethod(final Object result,
//				ConcurrentResultMethodParameter returnType) {
//			super(new Callable<Object>() {
//				@Override
//				public Object call() throws Exception {
//					if (result instanceof Exception) {
//						throw (Exception) result;
//					} else if (result instanceof Throwable) {
//						throw new NestedServletException(
//								"Async processing failed", (Throwable) result);
//					}
//					return result;
//				}
//			}, CALLABLE_METHOD);
//			setHandlerMethodReturnValueHandlers(ServletInvocableHandlerMethod.this.returnValueHandlers);
//			this.returnType = returnType;
//		}
//
//		/**
//		 * Bridge to actual controller type-level annotations.
//		 */
//		@Override
//		public Class<?> getBeanType() {
//			return ServletInvocableHandlerMethod.this.getBeanType();
//		}
//
//		/**
//		 * Bridge to actual return value or generic type within the declared
//		 * async return type, e.g. Foo instead of {@code DeferredResult<Foo>}.
//		 */
//		@Override
//		public MethodParameter getReturnValueType(Object returnValue) {
//			return this.returnType;
//		}
//
//		/**
//		 * Bridge to controller method-level annotations.
//		 */
//		@Override
//		public <A extends Annotation> A getMethodAnnotation(
//				Class<A> annotationType) {
//			return ServletInvocableHandlerMethod.this
//					.getMethodAnnotation(annotationType);
//		}
//	}
//
//	/**
//	 * MethodParameter subclass based on the actual return value type or if
//	 * that's null falling back on the generic type within the declared async
//	 * return type, e.g. Foo instead of {@code DeferredResult<Foo>}.
//	 */
//	private class ConcurrentResultMethodParameter extends
//			HandlerMethodParameter {
//
//		private final Object returnValue;
//
//		private final ResolvableType returnType;
//
//		public ConcurrentResultMethodParameter(Object returnValue) {
//			super(-1);
//			this.returnValue = returnValue;
//			this.returnType = ResolvableType.forType(
//					super.getGenericParameterType()).getGeneric(0);
//		}
//
//		@Override
//		public Class<?> getParameterType() {
//			return (this.returnValue != null ? this.returnValue.getClass()
//					: this.returnType.getRawClass());
//		}
//
//		@Override
//		public Type getGenericParameterType() {
//			return this.returnType.getType();
//		}
//	}
//
//}