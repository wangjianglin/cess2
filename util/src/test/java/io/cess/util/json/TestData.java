package io.cess.util.json;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用数据结构
 * @author 王江林
 * @date 2014年1月3日 下午1:41:54
 *
 */
public class TestData {

	private String name;
	
	private String password;

	private TestData[] array;
	private List<String> names = new ArrayList<String>();
	private List<TestData> namesList = new ArrayList<TestData>();
	
	private Map<String,Integer> map =new HashMap<String, Integer>();
	
	private Map<String,TestData> maps = new HashMap<String, TestData>();

	public String getName() {
		return name;
	}

	public TestData[] getArray() {
		return array;
	}

	public void setArray(TestData[] array) {
		this.array = array;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<TestData> getNamesList() {
		return namesList;
	}

	public void setNamesList(List<TestData> namesList) {
		this.namesList = namesList;
	}

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	public Map<String, TestData> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, TestData> maps) {
		this.maps = maps;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		//return super.equals(obj);
//		if(!(obj instanceof TestData)){
//			return false;
//		}
//		try {
//			return compare(this,(TestData)obj);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	@SuppressWarnings("null")
	public static boolean compare(Object data1,Object data2) throws Exception{
		if(data1 == null && data2 == null){
			return true;
		}
		
		if(!(data1 != null && data2 != null)){
			return false;
		}
		if(!data1.getClass().equals(data2.getClass())){
			return false;
		}
		
		Class<?> type = data1.getClass();
		//如果是一个数组
    	if(type.isArray()){
    		if(Array.getLength(data1) != Array.getLength(data2)){
    			return false;
    		}
    			for(int n=0;n<Array.getLength(data1);n++){
    				if(!compare(Array.get(data1, n), Array.get(data2, n))){
    					return false;
    				}
    			}
    		
    		return true;
    	}
    	
		if(List.class.isAssignableFrom(type)){
			@SuppressWarnings("rawtypes")
			List v1 = (List) data1;
			@SuppressWarnings("rawtypes")
			List v2 = (List) data2;
//			if(v1 == null && v2 == null){
//				return true;
//			}
//			if(!(v1 == null ||v2 == null)){
//				return false;
//			}
			if(v1.size() != v2.size()){
				return false;
			}
			for(int n=0;n<v1.size();n++){
				if(!compare(v1.get(n),v2.get(n))){
					return false;
				}
			}
			return true;
		}
		if(Map.class.isAssignableFrom(type)){
			@SuppressWarnings("rawtypes")
			Map v1 = (Map) data1;
			@SuppressWarnings("rawtypes")
			Map v2 = (Map) data2;
//			if(v1 == null && v2 == null){
//				return true;
//			}
//			if(!(v1 == null && v2 == null)){
//				return false;
//			}
			if(v1.size() != v2.size()){
				return false;
			}
			for(Object key: v1.keySet()){
				if(!compare(v1.get(key),v2.get(key))){
					return false;
				}
			}
			return true;
		}
		java.beans.BeanInfo infos = Introspector.getBeanInfo(type);
		for(PropertyDescriptor pd : infos.getPropertyDescriptors()){
			//pd.setValue(pd.getName(), deserializeImpl(map.get(pd.getName()),pd.getPropertyType()));
			if(pd.getReadMethod() != null && !pd.getReadMethod().getDeclaringClass().equals(Object.class)){
				//Type ttype = pd.getPropertyType();pd.getWriteMethod().getGenericParameterTypes()
				//System.out.println("type:"+ttype);
				if(!compare(pd.getReadMethod().invoke(data1),pd.getReadMethod().invoke(data2))){
					return false;
				}
				//pd.getWriteMethod().invoke(value, deserializeImpl(map.get(pd.getName()),mapType));
			}
		}
		return true;
	}
}
