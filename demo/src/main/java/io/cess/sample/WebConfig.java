//package lin.sample;
//
//import lin.core.spring.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.format.Formatter;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.format.support.FormattingConversionServiceFactoryBean;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
//import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
//import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by lin on 25/10/2016.
// */
//
//@Configuration
//@EnableWebMvc
////@ComponentScan(value = {"lin"},includeFilters = @ComponentScan.Filter(value=org.springframework.stereotype.Controller.class,type= FilterType.ANNOTATION),
////excludeFilters = {@ComponentScan.Filter(value=javax.inject.Named.class,type= FilterType.ANNOTATION),
////        @ComponentScan.Filter(value=javax.inject.Inject.class,type= FilterType.ANNOTATION),
////        @ComponentScan.Filter(value=org.springframework.stereotype.Service.class,type= FilterType.ANNOTATION),
////        @ComponentScan.Filter(value=org.springframework.stereotype.Component.class,type= FilterType.ANNOTATION),
////        @ComponentScan.Filter(value=org.springframework.stereotype.Repository.class,type= FilterType.ANNOTATION)})
//
//public class WebConfig extends WebMvcConfigurerAdapter {
//
////    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer){
////        // 在此处配置 ContentNegotiatingViewResolver
////    }
//@Override
//public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
//    configurer.enable();
//}
//
//
//    @Bean
//    public FormattingConversionServiceFactoryBean conversionService() {
//        FormattingConversionServiceFactoryBean conversionService = new FormattingConversionServiceFactoryBean();
//        Set<DateConverter> set = new HashSet<DateConverter>();
//        set.add(new lin.core.spring.DateConverter());
//        conversionService.setConverters(set);
//        return conversionService;
//    }
//
//    @Bean
//    public ConfigurableWebBindingInitializer webBindingIninializer(){
//        ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
//        webBindingInitializer.setConversionService(conversionService().getObject());
//        return webBindingInitializer;
//    }
//
//    @Bean
//    public LinMappingHandlerAdapter mappingHandlerAdapter(){
//        LinMappingHandlerAdapter mappingHandlerAdapter = new LinMappingHandlerAdapter();
//        mappingHandlerAdapter.setOrder(-100);
//        mappingHandlerAdapter.setWebBindingInitializer(webBindingIninializer());
//
//        mappingHandlerAdapter.setMessageConverters(Arrays.asList(messageConverter()));
//
//
//        mappingHandlerAdapter.setCustomArgumentResolvers(Arrays.asList(new LinPrefixMethodProcessor()));
//
//        mappingHandlerAdapter.setCustomFirstReturnValueHandlers(Arrays.asList(new LinMessageBodyMethodProcessor()));
//
//        return mappingHandlerAdapter;
//
//
//    }
//
//    @Bean
//    public LinMessageConverter messageConverter(){
//        LinMessageConverter messageConverter = new LinMessageConverter();
//        return messageConverter;
//    }
//
//    @Bean
//    public CessExceptionHandler exceptionHandler(){
//        CessExceptionHandler exceptionHandler = new CessExceptionHandler();
//        return exceptionHandler;
//    }
//
////    @Bean
////    public InternalResourceViewResolver viewResolver() {
////        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
////        resolver.setPrefix("WEB-INF/jsp/");
////        resolver.setSuffix(".jsp");
////        return resolver;
////    }
////
////	<bean id="exceptionResolver" class="lin.core.spring.CessExceptionHandler"/>
////
////    @Bean
////    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
////        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
////        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text/html;charset=UTF-8")));
////        return mappingJackson2HttpMessageConverter;
////    }
////	<bean id="mappingJacksonHttpMessageConverter"
////    class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
////		<property name="supportedMediaTypes">
////			<list>
////				<value>text/html;charset=UTF-8</value>
////			</list>
////		</property>
////	</bean>
//
//
//}