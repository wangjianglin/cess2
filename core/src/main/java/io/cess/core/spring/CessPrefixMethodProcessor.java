package io.cess.core.spring;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;

/**
 * 
 * @author lin
 * @date Jul 3, 2015 2:38:24 AM
 *
 */
public class CessPrefixMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

	private ServletModelAttributeMethodProcessor proxy = new ServletModelAttributeMethodProcessor(false);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(Prefix.class)) {
			return true;
		}
		return false;
	}
	
	private static Map<String,Class<?>> clesses = new HashMap<>();
	private static Object clessesLock = new Object();
	
	
	private String classPerfixName(Class<?> type){
		if(type.isArray()){
			return classPerfixName(type.getComponentType()) + "s";
		}
		return type.getName();
	}
	
	public final Class<?> createParameterClass(Class<?> type,String perfix) throws Exception {

		//创建对象
		ClassPool cp = ClassPool.getDefault();
		
		if(type.getPackage() != null){
			cp.importPackage(type.getPackage().getName());
		}
		
		String className = this.getClass().getPackage().getName() + "." + classPerfixName(type).replace(".", "") + "." + perfix + "__$$javassist$$__";
		Class<?> r = clesses.get(className);
		
		if(r != null){
			return r;
		}
		CtClass ctClass = cp.makeClass(className);
		
		cp.appendClassPath(new javassist.ClassClassPath(type));
		
		CtClass fclass = cp.get(type.getName());
		CtField ctField = new CtField(fclass,perfix,ctClass);
		ctField.setModifiers(Modifier.PRIVATE);
		//设置name属性的get set方法  
		String s = perfix.substring(0, 1).toUpperCase() + perfix.substring(1);
		ctClass.addMethod(CtNewMethod.setter("set" + s,ctField));
		ctClass.addMethod(CtNewMethod.getter("get" + s,ctField));
		ctClass.addField(ctField);
		
		//参数  1：参数类型   2：所属类CtClass  
		CtConstructor ctConstructor=new CtConstructor(new CtClass[]{},ctClass);
		ctConstructor.setBody(null);
		ctConstructor.setModifiers(Modifier.PUBLIC);
		ctClass.addConstructor(ctConstructor);
	
		synchronized (clessesLock) {
			r = clesses.get(className);
			if(r != null){
				return r;
			}
			r = ctClass.toClass();
			clesses.put(className, r);
		}
		return r;

	}
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return proxy.supportsReturnType(returnType);
	}
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		proxy.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}
	
	private static String listClassName(Type type){
		if(ParameterizedType.class.isAssignableFrom(type.getClass())){
			return "[" + listClassName(((ParameterizedType)type).getActualTypeArguments()[0]);
		}
		Class<?> c= (Class<?>) type;
		if(c.isPrimitive()){
			return c.getName();
		}
		return "L" + c.getName() + ";";
	}
	
	@SuppressWarnings("unchecked")
	private static Object listObject(Object obj){
		if(obj == null){
			return obj;
		}
		Class<?> c = obj.getClass();
		if(!c.isArray()){
			return obj;
		}
		@SuppressWarnings("rawtypes")
		List list = new ArrayList();
		for(int n=0;n<java.lang.reflect.Array.getLength(obj);n++){
			list.add(listObject(java.lang.reflect.Array.get(obj, n)));
		}
		return list;
	}
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		Prefix perfix = parameter.getParameterAnnotation(Prefix.class);
		String perfixString = null;
		if(perfix.value() == null || "".equals(perfix.value())){
			perfixString = parameter.getParameterName();
		}else{
			perfixString = perfix.value();
		}
		Class<?> type = parameter.getParameterType();
		
		if(List.class.isAssignableFrom(type)){
			type = Class.forName(listClassName(parameter.getGenericParameterType()));
		}
		Class<?> c = createParameterClass(type,perfixString);
//		System.out.println("obj:"+c.newInstance());
		Object obj = proxy.resolveArgument(new MethodParameter(parameter){
			public Class<?> getParameterType() {
				return c;
			}
			public Type getGenericParameterType() {
				return null;
			}
		}, mavContainer, webRequest, binderFactory);
		String s = parameter.getParameterName().substring(0, 1).toUpperCase() + parameter.getParameterName().substring(1);
		Method m = obj.getClass().getMethod("get" + s);
		Object argObj = m.invoke(obj);
		if(List.class.isAssignableFrom(parameter.getParameterType())){
			argObj = listObject(argObj);
		}
		return argObj;
	}

	
//	/**
//	 * Obtain a value from the request that may be used to instantiate the
//	 * model attribute through type conversion from String to the target type.
//	 * <p>The default implementation looks for the attribute name to match
//	 * a URI variable first and then a request parameter.
//	 * @param attributeName the model attribute name
//	 * @param request the current request
//	 * @return the request value to try to convert or {@code null}
//	 */
//	protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
//		Map<String, String> variables = getUriTemplateVariables(request);
//		if (StringUtils.hasText(variables.get(attributeName))) {
//			return variables.get(attributeName);
//		}
//		else if (StringUtils.hasText(request.getParameter(attributeName))) {
//			return request.getParameter(attributeName);
//		}
//		else {
//			return null;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
//		Map<String, String> variables =
//				(Map<String, String>) request.getAttribute(
//						HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
//		return (variables != null ? variables : Collections.<String, String>emptyMap());
//	}
//
//	/**
//	 * Create a model attribute from a String request value (e.g. URI template
//	 * variable, request parameter) using type conversion.
//	 * <p>The default implementation converts only if there a registered
//	 * {@link Converter} that can perform the conversion.
//	 * @param sourceValue the source value to create the model attribute from
//	 * @param attributeName the name of the attribute, never {@code null}
//	 * @param parameter the method parameter
//	 * @param binderFactory for creating WebDataBinder instance
//	 * @param request the current request
//	 * @return the created model attribute, or {@code null}
//	 * @throws Exception
//	 */
//	protected Object createAttributeFromRequestValue(String sourceValue, String attributeName,
//			MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
//			throws Exception {
//
//		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
//		ConversionService conversionService = binder.getConversionService();
//		if (conversionService != null) {
//			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
//			TypeDescriptor target = new TypeDescriptor(parameter);
//			if (conversionService.canConvert(source, target)) {
//				return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * This implementation downcasts {@link WebDataBinder} to
//	 * {@link ServletRequestDataBinder} before binding.
//	 * @see ServletRequestDataBinderFactory
//	 */
////	@Override
//	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
//		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
//		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
//		servletBinder.bind(servletRequest);
//	}
//
//	//-----------------------------------------------------------
//	protected Log logger = LogFactory.getLog(this.getClass());
//
//	private final boolean annotationNotRequired;
//
//
//	/**
//	 * @param annotationNotRequired if "true", non-simple method arguments and
//	 * return values are considered model attributes with or without a
//	 * {@code @ModelAttribute} annotation.
//	 */
////	public LinServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
//	public LinServletModelAttributeMethodProcessor() {
//		this.annotationNotRequired = false;
//	}
//
//
//	/**
//	 * @return true if the parameter is annotated with {@link ModelAttribute}
//	 * or in default resolution mode also if it is not a simple type.
//	 */
//	@Override
//	public boolean supportsParameter(MethodParameter parameter) {
//		if (parameter.hasParameterAnnotation(Prefix.class)) {
//			return true;
//		}
//		return false;
////		else if (this.annotationNotRequired) {
////			return !BeanUtils.isSimpleProperty(parameter.getParameterType());
////		}
////		else {
////			return false;
////		}
//	}
//
//	/**
//	 * Resolve the argument from the model or if not found instantiate it with
//	 * its default if it is available. The model attribute is then populated
//	 * with request values via data binding and optionally validated
//	 * if {@code @java.validation.Valid} is present on the argument.
//	 * @throws BindException if data binding and validation result in an error
//	 * and the next method parameter is not of type {@link Errors}.
//	 * @throws Exception if WebDataBinder initialization fails.
//	 */
//	@Override
//	public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//
////		String name = ModelFactory.getNameForParameter(parameter);
//		String name = parameter.getParameterName();
//		Object attribute = (mavContainer.containsAttribute(name) ?
//				mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, webRequest));
//
//		WebDataBinder binder = null;
//		if(binderFactory instanceof LinServletRequestDataBinderFactory){
//			binder = ((LinServletRequestDataBinderFactory)binderFactory).linCreateBinder(webRequest, attribute, name);
//		}
//		if (binder.getTarget() != null) {
//			bindRequestParameters(binder, webRequest);
//			validateIfApplicable(binder, parameter);
//			if (binder.getBindingResult().hasErrors()) {
//				if (isBindExceptionRequired(binder, parameter)) {
//					throw new BindException(binder.getBindingResult());
//				}
//			}
//		}
//
//		// Add resolved attribute and BindingResult at the end of the model
//
//		Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
//		mavContainer.removeAttributes(bindingResultModel);
//		mavContainer.addAllAttributes(bindingResultModel);
//
//		return binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
//	}
//
//	/**
//	 * Extension point to create the model attribute if not found in the model.
//	 * The default implementation uses the default constructor.
//	 * @param attributeName the name of the attribute (never {@code null})
//	 * @param parameter the method parameter
//	 * @param binderFactory for creating WebDataBinder instance
//	 * @param request the current request
//	 * @return the created model attribute (never {@code null})
//	 */
//	protected Object createAttribute2(String attributeName, MethodParameter parameter,
//			WebDataBinderFactory binderFactory,  NativeWebRequest request) throws Exception {
//
//		Class<?> type = parameter.getParameterType();
//		if(List.class.isAssignableFrom(type)){
//			type = ArrayList.class;
////			type.get
////			return typ
//		}else if(Map.class.isAssignableFrom(type)){
//			type = HashMap.class;
//		}
//		return BeanUtils.instantiateClass(type);
//	}
//
//	/**
//	 * Extension point to bind the request to the target object.
//	 * @param binder the data binder instance to use for the binding
//	 * @param request the current request
//	 */
//	protected void bindRequestParameters2(WebDataBinder binder, NativeWebRequest request) {
//		((WebRequestDataBinder) binder).bind(request);
//	}
//
//	/**
//	 * Validate the model attribute if applicable.
//	 * <p>The default implementation checks for {@code @javax.validation.Valid}.
//	 * @param binder the DataBinder to be used
//	 * @param parameter the method parameter
//	 */
//	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
//		Annotation[] annotations = parameter.getParameterAnnotations();
//		for (Annotation ann : annotations) {
//			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
//			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
//				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
//				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
//				binder.validate(validationHints);
//				break;
//			}
//		}
//	}
//
//	/**
//	 * Whether to raise a {@link BindException} on validation errors.
//	 * @param binder the data binder used to perform data binding
//	 * @param parameter the method argument
//	 * @return {@code true} if the next method argument is not of type {@link Errors}.
//	 */
//	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
//		int i = parameter.getParameterIndex();
//		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
//		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
//
//		return !hasBindingResult;
//	}
//
//	/**
//	 * Return {@code true} if there is a method-level {@code @ModelAttribute}
//	 * or if it is a non-simple type when {@code annotationNotRequired=true}.
//	 */
//	@Override
//	public boolean supportsReturnType(MethodParameter returnType) {
//		if (returnType.getMethodAnnotation(Prefix.class) != null) {
//			return true;
//		}
//		else if (this.annotationNotRequired) {
//			return !BeanUtils.isSimpleProperty(returnType.getParameterType());
//		}
//		else {
//			return false;
//		}
//	}
//
//	/**
//	 * Add non-null return values to the {@link ModelAndViewContainer}.
//	 */
//	@Override
//	public void handleReturnValue(Object returnValue, MethodParameter returnType,
//			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
//
//		if (returnValue != null) {
//			String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
//			mavContainer.addAttribute(name, returnValue);
//		}
//	}

}



//import java.beans.PropertyDescriptor;
//import java.beans.PropertyEditor;
//import java.lang.reflect.Field;
//import java.security.AccessControlContext;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.BeanWrapper;
//import org.springframework.beans.BeanWrapperImpl;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.PropertyValue;
//import org.springframework.beans.PropertyValues;
//import org.springframework.beans.TypeMismatchException;
//import org.springframework.core.MethodParameter;
//import org.springframework.core.convert.ConversionService;
//import org.springframework.core.convert.TypeDescriptor;
//
//public class LinBeanWrapperImpl implements BeanWrapper {
//
//	private BeanWrapperImpl proxy;
//	public LinBeanWrapperImpl() {
//		proxy = new BeanWrapperImpl();
//	}
//
//	public LinBeanWrapperImpl(boolean registerDefaultEditors) {
//		proxy = new BeanWrapperImpl(registerDefaultEditors);
//	}
//
//	public LinBeanWrapperImpl(Class<?> clazz) {
//		proxy = new BeanWrapperImpl(clazz);
//	}
//
//	public LinBeanWrapperImpl(Object object, String nestedPath,
//			Object rootObject) {
//		proxy = new BeanWrapperImpl(object, nestedPath, rootObject);
//	}
//
//	public LinBeanWrapperImpl(Object object) {
//		proxy = new BeanWrapperImpl(object);
//	}
//
//	private String _prefix;
//	private String prefix;
//	private String propertyPathRemovePrefix(String propertyPath) {
//		
//		if(propertyPath.startsWith(_prefix)){
//			return propertyPath.substring(this._prefix.length());
//		}
//		return propertyPath;
//	}
//	private PropertyValue propertyPathRemovePrefix(PropertyValue pv) {
//		
//		return new PropertyValue(pv){
//	
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//	
//			@Override
//			public String getName() {
//				return propertyPathRemovePrefix(super.getName());
//			}
//			
//		};
//	}
//
//	public String getPrefix() {
//		return prefix;
//	}
//
//	public void setPrefix(String prefix) {
//		this.prefix = prefix;
//		this._prefix = prefix;
//		if(this._prefix == null || "".equals(this._prefix)){
//			this._prefix = "";
//		}else{
//			this._prefix += ".";
//		}
//	}
//
//	
//	public final Object getWrappedInstance() {
//		return proxy.getWrappedInstance();
//	}
//
////	@Override
//	public final Class<?> getWrappedClass() {
//		return proxy.getWrappedClass();
//	}
//
//	/**
//	 * Return the nested path of the object wrapped by this BeanWrapper.
//	 */
//	public final String getNestedPath() {
//		return proxy.getNestedPath();
//	}
//
//	/**
//	 * Return the root object at the top of the path of this BeanWrapper.
//	 * @see #getNestedPath
//	 */
//	public final Object getRootInstance() {
//		return proxy.getRootInstance();
//	}
//
//	/**
//	 * Return the class of the root object at the top of the path of this BeanWrapper.
//	 * @see #getNestedPath
//	 */
//	public final Class<?> getRootClass() {
//		return proxy.getWrappedClass();
//	}
//
//
//
//	/**
//	 * Specify a limit for array and collection auto-growing.
//	 * <p>Default is unlimited on a plain BeanWrapper.
//	 */
//	@Override
//	public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
//		proxy.setAutoGrowCollectionLimit(autoGrowCollectionLimit);
//	}
//
//	/**
//	 * Return the limit for array and collection auto-growing.
//	 */
//	@Override
//	public int getAutoGrowCollectionLimit() {
//		return proxy.getAutoGrowCollectionLimit();
//	}
//
//	/**
//	 * Set the security context used during the invocation of the wrapped instance methods.
//	 * Can be null.
//	 */
//	public void setSecurityContext(AccessControlContext acc) {
//		proxy.setSecurityContext(acc);
//	}
//
//	/**
//	 * Return the security context used during the invocation of the wrapped instance methods.
//	 * Can be null.
//	 */
//	public AccessControlContext getSecurityContext() {
//		return proxy.getSecurityContext();
//	}
//
//
//	@Override
//	public PropertyDescriptor[] getPropertyDescriptors() {
//		return proxy.getPropertyDescriptors();
//	}
//
//	@Override
//	public PropertyDescriptor getPropertyDescriptor(String propertyName) throws BeansException {
//		if(!propertyName.startsWith(this._prefix)){
//			return null;
//		}
//		return proxy.getPropertyDescriptor(propertyPathRemovePrefix(propertyName));
//	}
//
//	@Override
//	public Class<?> getPropertyType(String propertyName) throws BeansException {
//		if(!propertyName.startsWith(this._prefix)){
//			return null;
//		}
//		return proxy.getPropertyType(propertyPathRemovePrefix(propertyName));
//	}
//
//	@Override
//	public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
//		if(!propertyName.startsWith(this._prefix)){
//			return null;
//		}
//		return proxy.getPropertyTypeDescriptor(propertyPathRemovePrefix(propertyName));
//	}
//
//	@Override
//	public boolean isReadableProperty(String propertyName) {
//		if(!propertyName.startsWith(this._prefix)){
//			return false;
//		}
//		return proxy.isReadableProperty(propertyPathRemovePrefix(propertyName));
//	}
//
//	@Override
//	public boolean isWritableProperty(String propertyName) {
//		if(!propertyName.startsWith(this._prefix)){
//			return false;
//		}
//		return proxy.isWritableProperty(propertyPathRemovePrefix(propertyName));
//	}
//
//	/**
//	 * Convert the given value for the specified property to the latter's type.
//	 * <p>This method is only intended for optimizations in a BeanFactory.
//	 * Use the {@code convertIfNecessary} methods for programmatic conversion.
//	 * @param value the value to convert
//	 * @param propertyName the target property
//	 * (note that nested or indexed properties are not supported here)
//	 * @return the new value, possibly the result of type conversion
//	 * @throws TypeMismatchException if type conversion failed
//	 */
//	public Object convertForProperty(Object value, String propertyName) throws TypeMismatchException {
//		if(!propertyName.startsWith(this._prefix)){
//			return null;
//		}
//		return proxy.convertForProperty(value, propertyPathRemovePrefix(propertyName));
//	}
//
//
//	@Override
//	public Object getPropertyValue(String propertyName) throws BeansException {
//		if(!propertyName.startsWith(this._prefix)){
//			return null;
//		}
//		return proxy.getPropertyValue(propertyPathRemovePrefix(propertyName));
//	}
//
//
//	@Override
//	public void setPropertyValue(String propertyName, Object value) throws BeansException {
//		if(!propertyName.startsWith(this._prefix)){
//			return;
//		}
//		proxy.setPropertyValue(propertyPathRemovePrefix(propertyName),value);
//	}
//
//	@Override
//	public void setPropertyValue(PropertyValue pv) throws BeansException {
//		if(!pv.getName().startsWith(this._prefix)){
//			return;
//		}
//		proxy.setPropertyValue(propertyPathRemovePrefix(pv));
//	}
//
//	@Override
//	public String toString() {
//		return proxy.toString();
//	}
//
//	@Override
//	public void setConversionService(ConversionService conversionService) {
//		proxy.setConversionService(conversionService);
//	}
//
//	@Override
//	public ConversionService getConversionService() {
//		return proxy.getConversionService();
//	}
//
//	@Override
//	public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
//		proxy.setExtractOldValueForEditor(extractOldValueForEditor);
//	}
//
//	@Override
//	public boolean isExtractOldValueForEditor() {
//		return proxy.isExtractOldValueForEditor();
//	}
//
//	@Override
//	public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
//		proxy.setAutoGrowNestedPaths(autoGrowNestedPaths);
//	}
//
//	@Override
//	public boolean isAutoGrowNestedPaths() {
//		return proxy.isAutoGrowNestedPaths();
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public void setPropertyValues(Map<?, ?> map) throws BeansException {
//		Map newMap = null;
//		if (map != null) {
//			newMap = new HashMap();
//			
////			this.propertyValueList = new ArrayList<PropertyValue>(original.size());
//			for (Map.Entry<?, ?> entry : map.entrySet()) {
////				this.propertyValueList.add(new PropertyValue(entry.getKey().toString(), entry.getValue()));
//				if(!((String)entry.getKey()).startsWith(this._prefix)){
//					continue;
//				}
//				newMap.put(propertyPathRemovePrefix(entry.getKey().toString()), entry.getValue());
//			}
//		}
//		proxy.setPropertyValues(newMap);
//	}
//
//	
//	private PropertyValues propertyValueshRemovePrefix(PropertyValues pvs) {
//		
//		return new PropertyValues(){
//	
//			@Override
//			public PropertyValue[] getPropertyValues() {
//				PropertyValue[] values = pvs.getPropertyValues();
//				if(values == null){
//					return null;
//				}
//				List<PropertyValue> newValues = new ArrayList<PropertyValue>();
//				for(int n=0;n<values.length;n++){
//					if(!values[n].getName().startsWith(LinBeanWrapperImpl.this._prefix)){
//						continue;
//					}
//					//newValues[n] = propertyPathRemovePrefix(values[n]);
//					newValues.add(propertyPathRemovePrefix(values[n]));
//				}
//				return newValues.toArray(new PropertyValue[]{});
//				//return pvs.getPropertyValues();
//			}
//
//			@Override
//			public PropertyValue getPropertyValue(String propertyName) {
//				return pvs.getPropertyValue(_prefix + propertyName);
//			}
//
//			@Override
//			public PropertyValues changesSince(PropertyValues old) {
//				return pvs.changesSince(old);
//			}
//
//			@Override
//			public boolean contains(String propertyName) {
//				return pvs.contains(_prefix + propertyName);
//			}
//
//			@Override
//			public boolean isEmpty() {
//				return pvs.isEmpty();
//			}
//			
//		};
//	}
//
//	@Override
//	public void setPropertyValues(PropertyValues pvs) throws BeansException {
//		proxy.setPropertyValues(propertyValueshRemovePrefix(pvs));
//	}
//
//	@Override
//	public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown)
//			throws BeansException {
//		proxy.setPropertyValues(propertyValueshRemovePrefix(pvs), ignoreUnknown);
//	}
//
//	@Override
//	public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown,
//			boolean ignoreInvalid) throws BeansException {
//		proxy.setPropertyValues(propertyValueshRemovePrefix(pvs), ignoreUnknown, ignoreInvalid);
//	}
//
//	@Override
//	public void registerCustomEditor(Class<?> requiredType,
//			PropertyEditor propertyEditor) {
//		proxy.registerCustomEditor(requiredType, propertyEditor);
//	}
//
//	@Override
//	public void registerCustomEditor(Class<?> requiredType,
//			String propertyPath, PropertyEditor propertyEditor) {
//		if(!propertyPath.startsWith(this._prefix)){
//			return;
//		}
//		proxy.registerCustomEditor(requiredType, this.propertyPathRemovePrefix(propertyPath), propertyEditor);
//	}
//
//	@Override
//	public PropertyEditor findCustomEditor(Class<?> requiredType,
//			String propertyPath) {
//		if(!propertyPath.startsWith(this._prefix)){
//			return null;
//		}
//		return proxy.findCustomEditor(requiredType, propertyPathRemovePrefix(propertyPath));
//	}
//
//	@Override
//	public <T> T convertIfNecessary(Object value, Class<T> requiredType)
//			throws TypeMismatchException {
//		return proxy.convertIfNecessary(value, requiredType);
//	}
//
//	@Override
//	public <T> T convertIfNecessary(Object value, Class<T> requiredType,
//			MethodParameter methodParam) throws TypeMismatchException {
//		return proxy.convertIfNecessary(value, requiredType, methodParam);
//	}
//
//	@Override
//	public <T> T convertIfNecessary(Object value, Class<T> requiredType,
//			Field field) throws TypeMismatchException {
//		return proxy.convertIfNecessary(value, requiredType, field);
//	}
//
//
////
////	/**
////	 * Inner class to avoid a hard dependency on Java 8.
////	 */
////	@UsesJava8
////	private static class OptionalUnwrapper {
////
////		public static Object unwrap(Object optionalObject) {
////			Optional<?> optional = (Optional<?>) optionalObject;
////			Assert.isTrue(optional.isPresent(), "Optional value must be present");
////			Object result = optional.get();
////			Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
////			return result;
////		}
////
////		public static boolean isEmpty(Object optionalObject) {
////			return !((Optional<?>) optionalObject).isPresent();
////		}
////	}
//}
