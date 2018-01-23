package io.cess.core.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.web.XmlBeamHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 
 * @author lin
 * @date Jul 5, 2015 6:28:49 PM
 *
 */
public class JsonBodyMethodProcessor implements HandlerMethodArgumentResolver,HandlerMethodReturnValueHandler {

	private JsonMessageConverter messageConverter = new JsonMessageConverter();

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), JsonBody.class) != null ||
                returnType.getMethodAnnotation(JsonBody.class) != null);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
		
//		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

		messageConverter.write(returnValue, MediaType.APPLICATION_JSON_UTF8, outputMessage);
	}
	
	protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		return new ServletServerHttpRequest(servletRequest);
	}
	
	protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		return new ServletServerHttpResponse(response);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return null;
	}

}
//	RequestMappingHandlerAdapter p;
////	private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");
////	private ResponseBodyAdviceChain adviceChain;
//	protected LinJsonBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
//		this(messageConverters, null);
//	}
//
//	protected LinJsonBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
//			ContentNegotiationManager manager) {
//		this(messageConverters, manager, null);
//	}
//
//	protected LinJsonBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
//			ContentNegotiationManager manager, List<Object> responseBodyAdvice) {
//
//		super(messageConverters);
//		this.contentNegotiationManager = (manager != null ? manager : new ContentNegotiationManager());
//		this.adviceChain = new ResponseBodyAdviceChain(responseBodyAdvice);
//	}
//
//
//	@Override
//	public boolean supportsParameter(MethodParameter parameter) {
//		return parameter.hasParameterAnnotation(RequestBody.class);
////		return false;
//	}
//
////	@Override
//	public boolean supportsReturnType(MethodParameter returnType) {
//		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null ||
//				returnType.getMethodAnnotation(ResponseBody.class) != null);
//	}
//
//	/**
//	 * @throws MethodArgumentNotValidException if validation fails
//	 * @throws HttpMessageNotReadableException if {@link RequestBody#required()}
//	 * is {@code true} and there is no body content or if there is no suitable
//	 * converter to read the content with.
//	 */
//	@Override
//	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//
//		Object argument = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
//		String name = Conventions.getVariableNameForParameter(parameter);
//		WebDataBinder binder = binderFactory.createBinder(webRequest, argument, name);
//		if (argument != null) {
//			validate(binder, parameter);
//		}
//		mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
//		return argument;
//	}
//
//	private void validate(WebDataBinder binder, MethodParameter parameter) throws Exception {
//		Annotation[] annotations = parameter.getParameterAnnotations();
//		for (Annotation ann : annotations) {
//			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
//			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
//				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
//				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
//				binder.validate(validationHints);
//				BindingResult bindingResult = binder.getBindingResult();
//				if (bindingResult.hasErrors()) {
//					if (isBindExceptionRequired(binder, parameter)) {
//						throw new MethodArgumentNotValidException(parameter, bindingResult);
//					}
//				}
//				break;
//			}
//		}
//	}
//
////	/**
////	 * Whether to raise a {@link MethodArgumentNotValidException} on validation errors.
////	 * @param binder the data binder used to perform data binding
////	 * @param parameter the method argument
////	 * @return {@code true} if the next method argument is not of type {@link Errors}.
////	 */
////	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
////		int i = parameter.getParameterIndex();
////		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
////		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
////
////		return !hasBindingResult;
////	}
//
//	@Override
//	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
//			MethodParameter methodParam,  Type paramType) throws IOException, HttpMediaTypeNotSupportedException {
//
//		final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//		HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);
//
//		InputStream inputStream = inputMessage.getBody();
//		if (inputStream == null) {
//			return handleEmptyBody(methodParam);
//		}
//		else if (inputStream.markSupported()) {
//			inputStream.mark(1);
//			if (inputStream.read() == -1) {
//				return handleEmptyBody(methodParam);
//			}
//			inputStream.reset();
//		}
//		else {
//			final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
//			int b = pushbackInputStream.read();
//			if (b == -1) {
//				return handleEmptyBody(methodParam);
//			}
//			else {
//				pushbackInputStream.unread(b);
//			}
//			inputMessage = new ServletServerHttpRequest(servletRequest) {
//				@Override
//				public InputStream getBody() throws IOException {
//					// Form POST should not get here
//					return pushbackInputStream;
//				}
//			};
//		}
//
//		return super.readWithMessageConverters(inputMessage, methodParam, paramType);
//	}
//
//	private Object handleEmptyBody(MethodParameter param) {
//		if (param.getParameterAnnotation(RequestBody.class).required()) {
//			throw new HttpMessageNotReadableException("Required request body content is missing: " + param);
//		}
//		return null;
//	}
//
////	@Override
//	public void handleReturnValue(Object returnValue, MethodParameter returnType,
//			ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
//			throws IOException, HttpMediaTypeNotAcceptableException {
//
//		mavContainer.setRequestHandled(true);
//
//		// Try even with null return value. ResponseBodyAdvice could get involved.
//		writeWithMessageConverters(returnValue, returnType, webRequest);
//	}
//	//------------------------------------------------------------------------------------------------------------------------
//	private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");
//
//	private final ContentNegotiationManager contentNegotiationManager;
//
//	private final ResponseBodyAdviceChain adviceChain;
//
//
//
////	protected ResponseBodyAdviceChain getAdviceChain() {
////		return this.adviceChain;
////	}
//
//	/**
//	 * Creates a new {@link HttpOutputMessage} from the given {@link NativeWebRequest}.
//	 * @param webRequest the web request to create an output message from
//	 * @return the output message
//	 */
//	protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
//		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//		return new ServletServerHttpResponse(response);
//	}
//
//	/**
//	 * Writes the given return value to the given web request. Delegates to
//	 * {@link #writeWithMessageConverters(Object, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse)}
//	 */
//	protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType, NativeWebRequest webRequest)
//			throws IOException, HttpMediaTypeNotAcceptableException {
//
//		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
//		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
//		writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
//	}
//
//	/**
//	 * Writes the given return type to the given output message.
//	 * @param returnValue the value to write to the output message
//	 * @param returnType the type of the value
//	 * @param inputMessage the input messages. Used to inspect the {@code Accept} header.
//	 * @param outputMessage the output message to write to
//	 * @throws IOException thrown in case of I/O errors
//	 * @throws HttpMediaTypeNotAcceptableException thrown when the conditions indicated by {@code Accept} header on
//	 * the request cannot be met by the message converters
//	 */
//	@SuppressWarnings("unchecked")
//	protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType,
//			ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
//			throws IOException, HttpMediaTypeNotAcceptableException {
//
//		Class<?> returnValueClass = getReturnValueType(returnValue, returnType);
//		HttpServletRequest servletRequest = inputMessage.getServletRequest();
//		List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(servletRequest);
//		List<MediaType> producibleMediaTypes = getProducibleMediaTypes(servletRequest, returnValueClass);
//
//		Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
//		for (MediaType requestedType : requestedMediaTypes) {
//			for (MediaType producibleType : producibleMediaTypes) {
//				if (requestedType.isCompatibleWith(producibleType)) {
//					compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
//				}
//			}
//		}
//		if (compatibleMediaTypes.isEmpty()) {
//			if (returnValue != null) {
//				throw new HttpMediaTypeNotAcceptableException(producibleMediaTypes);
//			}
//			return;
//		}
//
//		List<MediaType> mediaTypes = new ArrayList<MediaType>(compatibleMediaTypes);
//		MediaType.sortBySpecificityAndQuality(mediaTypes);
//
//		MediaType selectedMediaType = null;
//		for (MediaType mediaType : mediaTypes) {
//			if (mediaType.isConcrete()) {
//				selectedMediaType = mediaType;
//				break;
//			}
//			else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
//				selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
//				break;
//			}
//		}
//
//		if (selectedMediaType != null) {
//			selectedMediaType = selectedMediaType.removeQualityValue();
//			for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
//				if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
//					returnValue = this.adviceChain.invoke(returnValue, returnType, selectedMediaType,
//							(Class<HttpMessageConverter<?>>) messageConverter.getClass(), inputMessage, outputMessage);
////					if (returnValue != null) {
//						((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType, outputMessage);
//						if (logger.isDebugEnabled()) {
//							logger.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using [" +
//									messageConverter + "]");
//						}
////					}
//					return;
//				}
//			}
//		}
//
//		if (returnValue != null) {
//			throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
//		}
//	}
//
//	/**
//	 * Return the type of the value to be written to the response. Typically this
//	 * is a simple check via getClass on the returnValue but if the returnValue is
//	 * null, then the returnType needs to be examined possibly including generic
//	 * type determination (e.g. {@code ResponseEntity<T>}).
//	 */
//	protected Class<?> getReturnValueType(Object returnValue, MethodParameter returnType) {
//		return (returnValue != null ? returnValue.getClass() : returnType.getParameterType());
//	}
//
//	/**
//	 * Returns the media types that can be produced:
//	 * <ul>
//	 * <li>The producible media types specified in the request mappings, or
//	 * <li>Media types of configured converters that can write the specific return value, or
//	 * <li>{@link MediaType#ALL}
//	 * </ul>
//	 */
//	@SuppressWarnings("unchecked")
//	protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> returnValueClass) {
//		Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
//		if (!CollectionUtils.isEmpty(mediaTypes)) {
//			return new ArrayList<MediaType>(mediaTypes);
//		}
//		else if (!this.allSupportedMediaTypes.isEmpty()) {
//			List<MediaType> result = new ArrayList<MediaType>();
//			for (HttpMessageConverter<?> converter : this.messageConverters) {
//				if (converter.canWrite(returnValueClass, null)) {
//					result.addAll(converter.getSupportedMediaTypes());
//				}
//			}
//			return result;
//		}
//		else {
//			return Collections.singletonList(MediaType.ALL);
//		}
//	}
//
//	private List<MediaType> getAcceptableMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
//		List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
//		return (mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes);
//	}
//
//	/**
//	 * Return the more specific of the acceptable and the producible media types
//	 * with the q-value of the former.
//	 */
//	private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
//		MediaType produceTypeToUse = produceType.copyQualityValue(acceptType);
//		return (MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceTypeToUse) <= 0 ? acceptType : produceTypeToUse);
//	}
//}
//
//
//class ResponseBodyAdviceChain {
//
//	private static final Log logger = LogFactory.getLog(ResponseBodyAdviceChain.class);
//
//	private final List<Object> advice;
//
//
//	public ResponseBodyAdviceChain(List<Object> advice) {
//		this.advice = advice;
//	}
//
//
//	public boolean hasAdvice() {
//		return !CollectionUtils.isEmpty(this.advice);
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> T invoke(T body, MethodParameter returnType,
//			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
//			ServerHttpRequest request, ServerHttpResponse response) {
//
//		if (this.advice != null) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Invoking ResponseBodyAdvice chain for body=" + body);
//			}
//			for (Object advice : this.advice) {
//				if (advice instanceof ControllerAdviceBean) {
//					ControllerAdviceBean adviceBean = (ControllerAdviceBean) advice;
//					if (!adviceBean.isApplicableToBeanType(returnType.getContainingClass())) {
//						continue;
//					}
//					advice = adviceBean.resolveBean();
//				}
//				if (advice instanceof ResponseBodyAdvice) {
//					ResponseBodyAdvice<T> typedAdvice = (ResponseBodyAdvice<T>) advice;
//					if (typedAdvice.supports(returnType, selectedConverterType)) {
//						body = typedAdvice.beforeBodyWrite(body, returnType,
//								selectedContentType, selectedConverterType, request, response);
//					}
//				}
//				else {
//					throw new IllegalStateException("Expected ResponseBodyAdvice: " + advice);
//				}
//			}
//			if (logger.isDebugEnabled()) {
//				logger.debug("After ResponseBodyAdvice chain body=" + body);
//			}
//		}
//		return body;
//	}
//
//}
