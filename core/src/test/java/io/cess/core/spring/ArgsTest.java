//package lin.core.spring;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.springframework.beans.BeanWrapperImpl;
//import org.springframework.beans.PropertyValue;
//
//public class ArgsTest {
//
//	@Test
//	public void testArgs(){
////		TestEntity entity = new TestEntity();
////		LinBeanWrapperImpl bean = new LinBeanWrapperImpl(entity);
////		bean.setPrefix("entity");
////		bean.setAutoGrowNestedPaths(true);
////		bean.setAutoGrowCollectionLimit(256);
////		PropertyValue pv = null;
////
////		pv = new PropertyValue("entity.data.id","3");
////		bean.setPropertyValue(pv);
////
//////		pv = new PropertyValue("entity.date","2015-02-05 01:22:21");
////		bean.setPropertyValue(pv);
////
////		pv = new PropertyValue("entity.data.name","name");
////		bean.setPropertyValue(pv);
////
////		pv = new PropertyValue("entity.name","name");
////		bean.setPropertyValue(pv);
////
////		pv = new PropertyValue("entity.data.value","value");
////		bean.setPropertyValue(pv);
////
////		pv = new PropertyValue("entity.id","3");
////		bean.setPropertyValue(pv);
////
//////		pv = new PropertyValue("entity.data.date","2015-02-05 01:22:21");
////		bean.setPropertyValue(pv);
////
////		pv = new PropertyValue("entity.value","value");
////		bean.setPropertyValue(pv);
////
////		System.out.println("name:" + entity.getName());;
//	}
//
//	@Test
//	public void testArgs2(){
//		TestEntity entity = new TestEntity();
//		BeanWrapperImpl bean = new BeanWrapperImpl(entity);
////		bean.setPrefix("entity");
//		bean.setAutoGrowNestedPaths(true);
//		bean.setAutoGrowCollectionLimit(256);
//		PropertyValue pv = null;
//
//		pv = new PropertyValue("data.id","3");
//		bean.setPropertyValue(pv);
//
////		pv = new PropertyValue("date","2015-02-05 01:22:21");
//		bean.setPropertyValue(pv);
//
//		pv = new PropertyValue("data.name","name");
//		bean.setPropertyValue(pv);
//
//		pv = new PropertyValue("name","name");
//		bean.setPropertyValue(pv);
//
//		pv = new PropertyValue("data.value","value");
//		bean.setPropertyValue(pv);
//
//		pv = new PropertyValue("id","3");
//		bean.setPropertyValue(pv);
//
////		pv = new PropertyValue("data.date","2015-02-05 01:22:21");
//		bean.setPropertyValue(pv);
//
//		pv = new PropertyValue("value","value");
//		bean.setPropertyValue(pv);
//
//		System.out.println("name:" + entity.getName());;
//	}
//
//
//	@Test
//	public void testListArgs(){
////		List<String> entity = new ArrayList<String>();
//////		ListEntity entity = new ListEntity();
////		LinBeanWrapperImpl bean = new LinBeanWrapperImpl(entity);
////		bean.setPrefix("");
////		bean.setAutoGrowNestedPaths(true);
////		bean.setAutoGrowCollectionLimit(256);
////		PropertyValue pv = null;
////
//////		pv = new PropertyValue("ids[0]","3");
////		pv = new PropertyValue("[0]","3");
////		bean.setPropertyValue(pv);
////
//////		pv = new PropertyValue("ids[1]","3");
////		pv = new PropertyValue("[1]","3");
////		bean.setPropertyValue(pv);
////
////
//////		System.out.println("list size:" + entity.getIds().size());
////		System.out.println("list size:" + entity.size());
//	}
//}
//
//class ListEntity{
//	List<String> ids;
//
//	public List<String> getIds() {
//		return ids;
//	}
//
//	public void setIds(List<String> ids) {
//		this.ids = ids;
//	}
//
//}
