package io.cess.core.jpa;

import java.lang.reflect.Method;
import java.util.Locale;

import io.cess.util.json.ValueSerializer;


/**
 * 
 * @author 王江林
 * @date 2012-7-11 下午7:54:57
 *
 */
public class BOOLEANSerializer implements ValueSerializer{

	@Override
	public Object Serialize(Object value,Object parameter, Locale local) {
		BOOLEAN b = (BOOLEAN)value;
		if(b != null){
			if(b == BOOLEAN.TRUE){
				return true;
			}else{
				return false;
			}
		}
		return null;
	}

	@Override
	public Object Deserialize(Object value,Object parameter, Locale local) {
		return Enum.valueOf(BOOLEAN.class, value.toString().toUpperCase());
	}

	@Override
	public Method method() {
		return null;
	}


}
