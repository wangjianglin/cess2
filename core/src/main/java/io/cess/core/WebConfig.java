//package io.cess.core;
//
//import io.cess.core.jpa.JpaBeanNameGenerator;
//import io.cess.core.spring.*;
//import io.cess.core.jpa.JpaBeanNameGenerator;
//import io.cess.core.spring.*;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.format.support.FormattingConversionServiceFactoryBean;
//import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
//import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by lin on 29/10/2016.
// */
//@Configuration
//@EnableWebMvc
//@ConditionalOnWebApplication
//@ComponentScan(value = "lin.core",nameGenerator = JpaBeanNameGenerator.class)
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//
//    @Override
//    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
//
//
//    @Bean
//    public FormattingConversionServiceFactoryBean conversionService() {
//        FormattingConversionServiceFactoryBean conversionService = new FormattingConversionServiceFactoryBean();
//        Set<DateConverter> set = new HashSet<DateConverter>();
//        set.add(new DateConverter());
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
//    public CessMappingHandlerAdapter mappingHandlerAdapter(){
//        CessMappingHandlerAdapter mappingHandlerAdapter = new CessMappingHandlerAdapter();
//        mappingHandlerAdapter.setOrder(-100);
//        mappingHandlerAdapter.setWebBindingInitializer(webBindingIninializer());
//
////        mappingHandlerAdapter.setMessageConverters(Arrays.asList(new LinMessageConverter()));
//
//        mappingHandlerAdapter.setMessageConverters(Arrays.asList(new FastJsonHttpMessageConverter()));
//
//        mappingHandlerAdapter.setCustomArgumentResolvers(Arrays.asList(new CessPrefixMethodProcessor()));
//
//        mappingHandlerAdapter.setCustomFirstReturnValueHandlers(Arrays.asList(new CessMessageBodyMethodProcessor()));
//
//        return mappingHandlerAdapter;
//
//
//    }
//
//    //@Bean
////    public LinMessageConverter messageConverter(){
////        LinMessageConverter messageConverter = new LinMessageConverter();
////        return messageConverter;
////    }
//
////    @Autowired
////    private WebMvcProperties webMvcProperties;
////
////    @Override
////    public void configureViewResolvers(ViewResolverRegistry registry) {
////        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
////        viewResolver.setPrefix(webMvcProperties.getView().getPrefix());
////        viewResolver.setSuffix(webMvcProperties.getView().getSuffix());
////        registry.viewResolver(viewResolver);
////    }
//
//    @Bean
//    public FastJsonHttpMessageConverter jsonHttpMessageConverter(){
//        return new FastJsonHttpMessageConverter();
//    }
//
//    @Bean
//    public CessExceptionHandler exceptionHandler(){
//        CessExceptionHandler exceptionHandler = new CessExceptionHandler();
//        return exceptionHandler;
//    }
//
//
////    @Bean
////    @ConditionalOnMissingBean
////    @ConfigurationProperties(prefix = "spring.mvc")
////    public InternalResourceViewResolver internalResourceViewResolver () {
////        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
////        viewResolver.setPrefix("/WEB-INF/");
////        viewResolver.setSuffix(".jsp");
////        return viewResolver;
////    }
//
//}