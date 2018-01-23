package io.cess.core.util;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import io.cess.CessException;

/**
 * 
 * @author lin
 * @date 2012-3-13 下午8:47:09
 *
 *
 */
public class Properties{

	private Map<String,String> messages = new HashMap<String,String>();
	
	public Properties(String url){
		try {
			Enumeration<URL> urls = CessException.class.getClassLoader().getResources(url);
			URL tmpUrl = null;
			java.util.Properties prop = null;
			while(urls.hasMoreElements()){
				tmpUrl = urls.nextElement();
				prop = new java.util.Properties();
				prop.load(tmpUrl.openStream());
				for(Map.Entry<Object,Object> entity : prop.entrySet()){
					try{
						messages.put(entity.getKey().toString(), entity.getValue().toString());
					}catch(Exception e){
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getString(String name){
		return messages.get(name);
	}
}
