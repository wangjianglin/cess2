
方式一：

SpringApplication.run(new String[]{"classpath*:META-INF/lin/lin-spring-bean.xml","classpath*:META-INF/lin/lin-cxf-bean.xml"},args);


@Configuration
//@ImportResource(locations={"classpath*:META-INF/lin/lin-spring-bean.xml"})
public class DemoConfig {


    @Bean
    public ServletRegistrationBean springmvc(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new org.springframework.web.servlet.DispatcherServlet(),
        "*.action");

        //classpath*:/META-INF/lin/lin-spring-web.xml;classpath*:/META-INF/lin/lin-spring-security.xml
        bean.addInitParameter("contextConfigLocation","classpath*:/META-INF/lin/lin-spring-web.xml");

        bean.setLoadOnStartup(0);
        // Need to occur before the EmbeddedAtmosphereInitializer
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public ServletRegistrationBean cfx(){

        ServletRegistrationBean bean = new ServletRegistrationBean(new org.apache.cxf.transport.servlet.CXFServlet(),
                "/services/*");

        bean.setLoadOnStartup(1);
        // Need to occur before the EmbeddedAtmosphereInitializer
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }


}


方式二：

web.xml


<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath*:META-INF/lin/lin-spring-bean.xml;classpath*:META-INF/lin/lin-cxf-bean.xml</param-value>
  </context-param>



  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>

  </listener>

<listener>
        <listener-class>
            org.springframework.web.context.request.RequestContextListener
        </listener-class>
</listener>

<listener>
    <listener-class>lin.core.web.LoggingListener</listener-class>
</listener>

<servlet>
		<servlet-name>spring web</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath*:/META-INF/lin/lin-spring-web.xml;classpath*:/META-INF/lin/lin-spring-security.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>spring web</servlet-name>
		<url-pattern>*.action</url-pattern>
	</servlet-mapping>

	<!-- cxf servlet -->
	<servlet>
		<servlet-name>CXFServlet</servlet-name>
		<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>CXFServlet</servlet-name>
		<url-pattern>/services/*</url-pattern>
	</servlet-mapping>


</web-app>


方式三：



SpringBootServletInitializer




