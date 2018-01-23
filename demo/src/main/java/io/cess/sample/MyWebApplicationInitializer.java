package io.cess.sample;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;

/**
 * Created by lin on 9/3/16.
 */

@HandlesTypes(WebApplicationInitializer.class)
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) {
        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new
                DispatcherServlet());
        registration.setLoadOnStartup(1);
        registration.addMapping("*.action");
    }
}