package io.cess.core;

import java.net.URL;
import java.util.Enumeration;

import org.junit.Test;

public class JarNameFromClassTest {

	@Test
	public void testFarNameFromClass() throws Exception{
		Class<?> c = java.lang.System.class;
		String resourceName = c.getPackage().getName();
		resourceName = "/" + resourceName.replace('.', '/');
		resourceName = "/ad";
		System.out.println("resource name:"+resourceName);
		Enumeration<URL> urls = JarNameFromClassTest.class.getClassLoader().getResources(resourceName);
		while(urls.hasMoreElements()){
			System.out.println("url:"+urls.hasMoreElements());
		}
	}
}
