package io.cess.core.jpa;

import io.cess.util.json.JsonSerializer;


/**
 * 
 * @author 王江林
 * @date 2012-7-2 下午12:03:24
 *
 */
@JsonSerializer(serializer=BOOLEANSerializer.class)
public enum BOOLEAN {

	FALSE("false"),TRUE("true");
	
	private final String name;
	
	BOOLEAN(String name){
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
