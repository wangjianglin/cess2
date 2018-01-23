package io.cess.core.cxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import io.cess.core.ws.UrlPrefix;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.Bus;


/**
 * 
 * @author lin
 *
 */
public class RestHandlerMapping extends AbstractUrlHandlerMapping
  implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent>
{
	@Autowired
	private Bus bus;

	private String urlPrefix = "/rest/";

  public void setUrlPrefix(String urlPrefix) {
    this.urlPrefix = urlPrefix(urlPrefix);
  }
  
  private String urlPrefix(String urlPrefix){
	  if(urlPrefix == null || "".equals(urlPrefix) || "/".equals(urlPrefix)){
		  urlPrefix = "/rest/";
	  }else{
		  if(!urlPrefix.startsWith("/")){
			  urlPrefix = "/" + urlPrefix;
		  }
		  if(!urlPrefix.endsWith("/")){
			  urlPrefix = urlPrefix + "/";
		  }
	  }
	  return urlPrefix;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
  
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
  {
    Class<?> clazz = AopUtils.getTargetClass(bean);
    if (clazz.isAnnotationPresent(Path.class)) {
    	UrlPrefix up = clazz.getAnnotation(UrlPrefix.class);
    	String rest = null;
    	if(up != null){
    		rest = this.urlPrefix(up.value());
    	}else{
    		rest = this.urlPrefix;
    	}
    	List<Object> tmpBeans = beans.get(rest);
    	if(tmpBeans == null){
    		tmpBeans = new ArrayList<Object>();
    		beans.put(rest, tmpBeans);
    	}
    	tmpBeans.add(bean);
//      WebService ws = (WebService)clazz.getAnnotation(WebService.class);
    }
    else if (this.logger.isDebugEnabled()) {
      this.logger.debug("Rejected bean '" + beanName + "' since it has no Path annotation");
    }

    return bean;
  }
  
  private Map<String,List<Object>> beans = new HashMap<String,List<Object>>();
  @Override
  public void onApplicationEvent(ContextRefreshedEvent ev) {
      //防止重复执行。
      if(ev.getApplicationContext().getParent() == null){
       
    	  createAndPublishEndpoint();
      }
  }

  private void createAndPublishEndpoint()
  {
	  JAXRSServerFactoryBean factory = null;

      JSONProvider<Object> provider = new JSONProvider<Object>();
      provider.setDropRootElement(true);


      
	  for(String rest : beans.keySet()){
//		  factory = RuntimeDelegate.getInstance().createEndpoint( app, JAXRSServerFactoryBean.class );

		  factory = new JAXRSServerFactoryBean();
		  factory.setBus(bus);

	//      factory.setResourceClasses(Arrays.asList(StudentService.class,HelloWorld.class));
	      //factory.setResourceClasses(StudentService.class,HelloWorld.class);  
	      //factory.setServiceBeans( Arrays.asList(StudentService.class,HelloWorld.class) );
	      
	      factory.setProviders(Arrays.asList(provider));
	
	      
	      factory.setAddress(rest);
	
	      factory.setServiceBeans(beans.get(rest));
	
	      factory.create();
		  }
//      factory.setProviders( Arrays.asList(StudentServiceImpl.class,HelloWorldImpl.class));ResourceProvider
//      factory.setResourceProviders(Arrays.asList(new SingletonResourceProvider(new StudentServiceImpl()),new SingletonResourceProvider(new HelloWorldImpl())));
//      factory.setResourceProvider(StudentService.class, new SingletonResourceProvider(new StudentServiceImpl()));  
//      factory.setResourceProvider(HelloWorld.class, new SingletonResourceProvider(new HelloWorldImpl()));  
      
      
//      List<Class<?>> beanClasses = new ArrayList<Class<?>>();
//      for(int n=0;n<beans.size();n++){
//    	  beanClasses.add(AopUtils.getTargetClass(beans.get(n)));
//      }
//      factory.setModelBeansWithServiceClass(resources, sClasses);
//      factory.setServiceClass(AopUtils.getTargetClass(bean));
//      factory.setser
      
//    JaxWsServiceFactoryBean aegisServiceFactoryBean = new JaxWsServiceFactoryBean();
//
//    ServerFactoryBean serverFactoryBean = new ServerFactoryBean();
//    serverFactoryBean.setServiceBean(implementor);
//    serverFactoryBean.setServiceClass(AopUtils.getTargetClass(implementor));
//    serverFactoryBean.setAddress(url);
//
//    serverFactoryBean.setServiceFactory(aegisServiceFactoryBean);
//
//    //serverFactoryBean.getServiceFactory().getServiceConfigurations().add(0, new AegisServiceConfiguration());
//    serverFactoryBean.getServiceFactory().getServiceConfigurations().add(0, new JaxWsServiceConfiguration());
//    serverFactoryBean.create();
  }

}