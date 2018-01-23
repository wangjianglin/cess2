package io.cess.core.cxf;

import java.util.ArrayList;

import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.springframework.aop.config.AbstractInterceptorDrivenBeanDefinitionDecorator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
//import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author lin
 * @date 2014年11月17日 上午12:22:39
 *
 */
public class CxfServicesBeanDefinitionParser extends
AbstractSingleBeanDefinitionParser   {

//	@Override
//	protected BeanDefinition createInterceptorDefinition(Node node) {
//		return null;
//	}
	protected Class<?> getBeanClass(Element element) {
		//return ArrayList.class;
		try {
			return Class.forName("io.cess.demo.services.impl.HelloWorldImpl");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
//
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		//doParse(element, builder);
		//parserContext.getDelegate().getReaderContext().
//		BeanDefinition beanDefinition = parserContext.getContainingBeanDefinition();
		//builder.rootBeanDefinition(beanClass)
		BeanDefinitionBuilder rb = BeanDefinitionBuilder.rootBeanDefinition(SpringJAXRSServerFactoryBean.class);
//		System.out.println("ok."+rb);
	}

}