package io.cess.core.spring;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.cess.CessException;
import io.cess.core.Constants;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.json.AbstractJackson2View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

/**
 * 
 * @author lin
 * @date 2015年1月31日 下午7:22:00
 *
 */
//@Component
public class CessExceptionHandler implements HandlerExceptionResolver {


	private boolean hasProtocolHeader(HttpServletRequest request){
		return request.getHeader(Constants.HTTP_COMM_PROTOCOL) != null;
	}

	private boolean hasCessMethod(Object obj){
		if(!(obj instanceof HandlerMethod)){
			return false;
		}
		HandlerMethod handlerMethod = (HandlerMethod) obj;

		boolean r = (AnnotatedElementUtils.hasAnnotation(handlerMethod.getReturnType().getContainingClass(), CessBody.class) ||
				handlerMethod.getReturnType().hasMethodAnnotation(CessBody.class));
		r = r || (AnnotatedElementUtils.hasAnnotation(handlerMethod.getReturnType().getContainingClass(), JsonBody.class) ||
				handlerMethod.getReturnType().hasMethodAnnotation(JsonBody.class));

		r = r || (AnnotatedElementUtils.hasAnnotation(handlerMethod.getReturnType().getContainingClass(), XmlBody.class) ||
				handlerMethod.getReturnType().hasMethodAnnotation(XmlBody.class));

		r = r || handlerMethod.hasMethodAnnotation(CessBody.class);
		r = r || handlerMethod.hasMethodAnnotation(JsonBody.class);
		r = r || handlerMethod.hasMethodAnnotation(XmlBody.class);

		return r;
	}

	private boolean hasAccept(HttpServletRequest request){
		String accept = request.getHeader("Accept");
		if(accept == null || "".equals(accept.trim())){
			return false;
		}

		return accept.indexOf(Constants.MEDIA_TYPE_CESS) != -1;
	}

	private String isReturnXml(HttpServletRequest request,Object obj){

		String accept = request.getHeader("Accept");
		if(accept != null && !"".equals(accept.trim())){

			boolean r = accept.indexOf(Constants.MEDIA_TYPE_CESS_JSON) != -1;
			r = r || accept.indexOf(Constants.MEDIA_TYPE_CESS2_JSON) != -1;

			if(r){
				return "json";
			}

			r = accept.indexOf(Constants.MEDIA_TYPE_CESS_XML) != -1;
			r = r || accept.indexOf(Constants.MEDIA_TYPE_CESS2_XML) != -1;
			if(r){
				return "xml";
			}
		}

		if(!(obj instanceof HandlerMethod)){
			return "json";
		}
		HandlerMethod handlerMethod = (HandlerMethod) obj;

		if((AnnotatedElementUtils.hasAnnotation(handlerMethod.getReturnType().getContainingClass(), JsonBody.class) ||
				handlerMethod.getReturnType().hasMethodAnnotation(JsonBody.class))){
			return "xml";
		}

		if((AnnotatedElementUtils.hasAnnotation(handlerMethod.getReturnType().getContainingClass(), XmlBody.class) ||
				handlerMethod.getReturnType().hasMethodAnnotation(XmlBody.class))){
			return "xml";
		}

		return "json";
	}
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception ex) {


		if(!this.hasCessMethod(obj) && !this.hasProtocolHeader(request) && !this.hasAccept(request)){
			return null;
		}
		long code = -1;
		String message = "未知错误";
		if(ex instanceof CessException){
			CessException exception = (CessException) ex;
			code = exception.getCode();
			message = exception.getMessage();
		}
		
		ModelAndView mav = new ModelAndView();

		response.setHeader(Constants.HTTP_COMM_WITH_ERROR,"");

		Map<String,Object> attributesMap = new HashMap<>();

		attributesMap.put("code",code);
		attributesMap.put("message", message);
        if(request.getHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG) != null){
        	ByteArrayOutputStream _out = new ByteArrayOutputStream();
        	ex.printStackTrace(new PrintStream(_out));
            attributesMap.put("stackTrace",_out.toString());
            
            if(ex.getCause() != null){
            	_out = new ByteArrayOutputStream();
            	ex.getCause().printStackTrace(new PrintStream(_out));
				attributesMap.put("cause",_out.toString());
            }
        }
		List<CessException> warning = CessException.get();
		if(warning != null && warning.size() > 0){
			List<ErrorObj> list = new ArrayList<>();
			ErrorObj error = null;
			boolean debug = request.getHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG) != null;
			for(CessException item : warning){
				if(item == null){
					continue;
				}

				error = new ErrorObj();
				list.add(error);
				if (debug) {
					if (item.getCause() != null) {
						error.setCause(item.getCause().getMessage());
					}
					error.setStackTrace(io.cess.util.Utils.printStackTrace(item));
				}
				error.setMessage(item.getMessage());
				error.setCode(item.getCode());
			}

			attributesMap.put("warning", list);
		}
        //view.setAttributesMap(attributes);
		AbstractView view = null;
		if("xml".equals(isReturnXml(request,obj))){
			view = new MappingJackson2XmlView();
			Map<String,Object> xmlMap = new HashMap<>();
			xmlMap.put("error",attributesMap);
			view.setAttributesMap(xmlMap);
		}else{
			view = new MappingJackson2JsonView();
			view.setAttributesMap(attributesMap);
		}

        mav.setView(view);
        return mav;
	}

}