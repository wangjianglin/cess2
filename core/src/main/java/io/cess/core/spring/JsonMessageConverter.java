package io.cess.core.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.cess.core.Constants;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import io.cess.CessException;
import io.cess.core.Constants;
import io.cess.util.JsonUtil;


/**
 * 
 * @author lin
 * @date 2014年9月1日 上午12:29:18
 *
 */

public class JsonMessageConverter extends AbstractHttpMessageConverter<Object> {

    public final static Charset UTF8     = Charset.forName("UTF-8");

    private Charset             defaultCharset  = UTF8;

    private MediaType returnMediaType = new MediaType("application", "json",UTF8);;

//    private SerializerFeature[] features = new SerializerFeature[0];

    public JsonMessageConverter(){
        super(new MediaType("application", "cess+json",UTF8)
                ,new MediaType("application", "cess",UTF8)
                ,new MediaType("application", "cess2",UTF8)
                ,new MediaType("application", "cess2+json",UTF8));
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    @Override
    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
    }
//    public SerializerFeature[] getFeatures() {
//        return features;
//    }
//
//    public void setFeatures(SerializerFeature... features) {
//        this.features = features;
//    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
                                                                                               HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
                                                                             HttpMessageNotWritableException {
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//		boolean debug = request.getHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG) != null;
//		if("0.2".equals(request.getHeader("__http_comm_protocol__"))){
//			this.writeInternal02(obj, outputMessage,debug);
//		}else{
//			this.writeInternal01(obj, outputMessage,debug);
//		}
        outputMessage.getHeaders().setContentType(returnMediaType);
		Object result = Utils.writeMessage(obj,outputMessage);
		writeInternalImpl(result,outputMessage);
	}
//
//	private void writeInternal02(Object object, HttpOutputMessage outputMessage,boolean debug){
//		try{
//			List<ErrorObj> warning = warningList(debug);
//			if(warning == null || warning.isEmpty()){
//				this.writeInternalImpl(object, outputMessage);
//				return;
//			}
//
//			outputMessage.getHeaders().add(Constants.HTTP_COMM_WITH_WARNING, "");
//
//			ResultObj02 result = new ResultObj02();
//			result.setWarning(warning);
//			result.setResult(object);
//			this.writeInternalImpl(result, outputMessage);
//		}catch(Throwable e){
//			e.printStackTrace();
//		}
//	}
//
//	private void writeInternal01(Object obj, HttpOutputMessage outputMessage, boolean debug)throws IOException,
//			HttpMessageNotWritableException {
//
//
////        String text = JSON.toJSONString(obj, features);
//		ResultObj resultObj = new ResultObj();
//		resultObj.setResult(obj);
//		resultObj.setCode(0);
//
//		List<ErrorObj> errors = warningList(debug);
//		resultObj.setWarnings(errors);
//
//		writeInternalImpl(resultObj,outputMessage);
//	}
//
//	private List<ErrorObj> warningList(boolean debug){
//		List<ErrorObj> errors = new ArrayList<ErrorObj>();
//
//		List<CessException> warnings = CessException.get();
//		if(warnings != null) {
//			ErrorObj error;
//			for (CessException warning : warnings) {
//				error = new ErrorObj();
//				errors.add(error);
//				if (debug) {
//					if (warning.getCause() != null) {
//						error.setCause(warning.getCause().getMessage());
//					}
//					error.setStackTrace(stackTrace(warning));
//				}
//				error.setMessage(warning.getMessage());
//				error.setCode(warning.getCode());
//			}
//		}
//		return errors;
//	}
//
	private void writeInternalImpl(Object obj, HttpOutputMessage outputMessage)throws IOException,
			HttpMessageNotWritableException{
		OutputStream out = outputMessage.getBody();
        String text = JsonUtil.serialize(obj);
        Charset charset = outputMessage.getHeaders().getContentType().getCharset();
        if(charset == null){
            charset = defaultCharset;
        }
        byte[] bytes = text.getBytes(charset);
        out.write(bytes);
    }
//
//    private String stackTrace(Throwable e){
//    	if(e == null){
//    		return null;
//    	}
//    	ByteArrayOutputStream _out = new ByteArrayOutputStream();
//    	e.printStackTrace(new PrintStream(_out));
//
//    	return _out.toString();
//
//    }
//
//	public class ResultObj{
//		private Object result;
//		private long code;
//		private List<ErrorObj> warnings;
//		public Object getResult() {
//			return result;
//		}
//		public void setResult(Object result) {
//			this.result = result;
//		}
//		public long getCode() {
//			return code;
//		}
//		public void setCode(long code) {
//			this.code = code;
//		}
//		public List<ErrorObj> getWarnings() {
//			return warnings;
//		}
//		public void setWarnings(List<ErrorObj> warnings) {
//			this.warnings = warnings;
//		}
//
//
//	}
//	public class ResultObj02{
//		private Object result;
//		private List<ErrorObj> warning;
//		public Object getResult() {
//			return result;
//		}
//		public void setResult(Object result) {
//			this.result = result;
//		}
//
//		public List<ErrorObj> getWarning() {
//			return warning;
//		}
//
//		public void setWarning(List<ErrorObj> warning) {
//			this.warning = warning;
//		}
//	}


}

//public class LinMessageConverter implements HttpMessageConverter<Object>{
//
//	public LinMessageConverter(){
//	}
//	//extends org.springframework.http.converter.json.MappingJackson2HttpMessageConverter{
//	//private HttpMessageConverter<Object> jsonconverter = new MappingJackson2HttpMessageConverter();
//	private HttpMessageConverter<Object> jsonconverter = new JsonMessageConverter();
//	private HttpMessageConverter<Object> xmlconverter = new XMLMessageConverter();
//	private HttpMessageConverter<Object> converter(){
//		//ServletRequestAttributes a = null;
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//		if("xml".equals(request.getParameter("__result__"))){
//			return xmlconverter;
//		}
//		return jsonconverter;
//	}
//	@Override
//	public boolean canRead(Class<?> clazz, MediaType mediaType) {
//		//return converter().canRead(clazz, mediaType);
//		//return jsonconverter.canRead(clazz, mediaType);
//		return true;
//	}
//
//	@Override
//	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
//		//return converter().canWrite(clazz, mediaType);
//		//return jsonconverter.canWrite(clazz, mediaType);
//		return true;
//	}
//
//	@Override
//	public List<MediaType> getSupportedMediaTypes() {
//		//return converter().getSupportedMediaTypes();
//		ArrayList<MediaType> types = new ArrayList<MediaType>();
//		types.addAll(jsonconverter.getSupportedMediaTypes());
//		types.addAll(xmlconverter.getSupportedMediaTypes());
//		return Collections.unmodifiableList(types);
//	}
//
//	@Override
//	public Object read(Class<? extends Object> clazz,
//			HttpInputMessage inputMessage) throws IOException,
//			HttpMessageNotReadableException {
//		return converter().read(clazz, inputMessage);
//	}
//
//	@Override
//	public void write(Object t, MediaType contentType,
//			HttpOutputMessage outputMessage) throws IOException,
//			HttpMessageNotWritableException {
//		//converter().write(t, contentType, outputMessage);
//		converter().write(t, new MediaType("application","json",Charset.forName("UTF-8")), outputMessage);
//	}
//
//
//	private class JsonMessageConverter extends MappingJackson2HttpMessageConverter{
//
//		private JsonMessageConverter(){
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));
//			this.setObjectMapper(mapper);
////			mapper.convertValue(fromValue, toValueType)
//		}
//		@Override
//		protected void writeInternal(Object object,
//				HttpOutputMessage outputMessage) throws IOException,
//				HttpMessageNotWritableException {
//			
//			
//			
//			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//			String protocol = request.getHeader(Constants.HTTP_COMM_PROTOCOL);
//			if(protocol == null){
//				super.writeInternal(object, outputMessage);
//				return;
//			}
//			ByteArrayOutputStream _out = new ByteArrayOutputStream();
//			_out.write("{\"code\":0,\"dataType\":0,\"result\":".getBytes());
//			super.writeInternal(object, new HttpOutputMessage(){
//
//				@Override
//				public HttpHeaders getHeaders() {
//					return outputMessage.getHeaders();
//				}
//
//				@Override
//				public OutputStream getBody() throws IOException {
//					return _out;
//				}});
//			_out.write("}".getBytes());
////			String json = Base64.getEncoder().encodeToString(_out.toByteArray());
//			String json = _out.toString();
//	        //json =  java.net.URLEncoder.encode(json, "ascii");
//			outputMessage.getBody().write(json.getBytes());
//		}
//		
//	}
//	
//	private class XMLMessageConverter extends Jaxb2RootElementHttpMessageConverter{
//		
//	}
//}
