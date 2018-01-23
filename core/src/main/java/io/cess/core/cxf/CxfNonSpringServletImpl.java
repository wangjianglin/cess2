package io.cess.core.cxf;


import java.util.Arrays;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

public class CxfNonSpringServletImpl extends CXFNonSpringServlet {
	  
//    private static final String SERVICE_SUFFIX = "";// "Facade";  
  
    private static final long serialVersionUID = 8262880864551976903L;  
  
    @Override  
    protected void loadBus(ServletConfig servletConfig) {  
        super.loadBus(servletConfig);  
  

        
        
        
//        Map<Object,Object> extend = new HashMap<Object,Object>();  
//        extend.put("json", "application/json");  
//        extend.put("xml", "application/xml");  
        

        Application app = new Application();
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( app, JAXRSServerFactoryBean.class );
//        factory.setResourceClasses(Arrays.asList(StudentService.class,HelloWorld.class));
        //factory.setResourceClasses(StudentService.class,HelloWorld.class);  
        //factory.setServiceBeans( Arrays.asList(StudentService.class,HelloWorld.class) );
        
        JSONProvider<Object> provider = new JSONProvider<Object>();
        provider.setDropRootElement(true);
        factory.setProviders(Arrays.asList(provider));

        
        factory.setAddress( "/rest");
//        factory.setProviders( Arrays.asList(StudentServiceImpl.class,HelloWorldImpl.class));ResourceProvider
//        factory.setResourceProviders(Arrays.asList(new SingletonResourceProvider(new StudentServiceImpl()),new SingletonResourceProvider(new HelloWorldImpl())));
//        factory.setResourceProvider(StudentService.class, new SingletonResourceProvider(new StudentServiceImpl()));  
//        factory.setResourceProvider(HelloWorld.class, new SingletonResourceProvider(new HelloWorldImpl()));  
        factory.create();
        
        
        
//        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();  
//        sf.setResourceClasses(StudentService.class);  
//        sf.setResourceProvider(StudentService.class, new SingletonResourceProvider(new StudentServiceImpl()));  
//        //sf.setAddress("http://localhost:9000/"); 
//        sf.setAddress("/HelloWorld3"); 
//        sf.setExtensionMappings(extend);  
//        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);  
//        JAXRSBindingFactory factory = new JAXRSBindingFactory();  
//        factory.setBus(sf.getBus());  
//        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);  
//        sf.create(); 
//        
//        sf = new JAXRSServerFactoryBean(); 
//        
//        sf.setResourceClasses(HelloWorld.class);  
//        sf.setResourceProvider(HelloWorld.class, new SingletonResourceProvider(new HelloWorldImpl()));  
//        //sf.setAddress("http://localhost:9000/"); 
//        sf.setAddress("/HelloWorld3"); 
//        sf.setExtensionMappings(extend);  
//        manager = sf.getBus().getExtension(BindingFactoryManager.class);  
//        factory = new JAXRSBindingFactory();  
//        factory.setBus(sf.getBus());  
//        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);  
//        sf.create(); 
  
    }  
  
//    public JaxRsApiApplication jaxRsApiApplication() {
//    	return new JaxRsApiApplication();
//	}
      
}  