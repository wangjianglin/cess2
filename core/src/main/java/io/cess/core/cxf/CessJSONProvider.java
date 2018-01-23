package io.cess.core.cxf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.provider.json.JSONProvider;

public class CessJSONProvider extends JSONProvider<Object>{

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] anns, MediaType mt) {
//		return super.isWriteable(type, genericType, anns, mt);
		if(mt.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType()) && mt.getSubtype().equals(MediaType.APPLICATION_JSON_TYPE.getSubtype()))
		{
			return true;
		}
		return false;
	}

}
