//package io.cess.core.web;
//
//import io.cess.core.spring.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.format.support.FormattingConversionService;
//import org.springframework.format.support.FormattingConversionServiceFactoryBean;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//@Configuration
//@EnableWebMvc
////@PropertySource({"classpath:META-INF/lin/lin-default.properties","classpath:META-INF/lin/lin.properties"})
//public class WebConfig extends WebMvcConfigurationSupport {
//
////    @Bean("conversionService")
////    public FormattingConversionServiceFactoryBean conversionServiceFactoryBean(){
////        FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
////        factoryBean.setConverters(new HashSet<Converter>(Arrays.asList(new DateConverter())));
////        return factoryBean;
////    }
////
////    @Bean("webBindingInitializer")
////    public ConfigurableWebBindingInitializer webBindingInitializer(FormattingConversionServiceFactoryBean factoryBean){
////        ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
////        webBindingInitializer.setConversionService(factoryBean.getObject());
////        return webBindingInitializer;
////    }
////
////    @Override
////    protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
////        return this.requestMappingHandlerAdapter();
////    }
////
////    @Override
////    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
////        return super.createRequestMappingHandlerMapping();
////    }
//
////    @Bean
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
//        CessMappingHandlerAdapter mappingHandlerAdapter = new CessMappingHandlerAdapter();
//
////        mappingHandlerAdapter.setOrder(-100);
////        mappingHandlerAdapter.setWebBindingInitializer(this.webBindingInitializer(this.conversionServiceFactoryBean()));
//////        mappingHandlerAdapter.setMessageConverters(Arrays.asList(this.jsonMessageConverter()));
////        mappingHandlerAdapter.setCustomArgumentResolvers(Arrays.asList(this.prefixMethodProcessor()));
////        mappingHandlerAdapter.setCustomFirstReturnValueHandlers(Arrays.asList(this.jsonBodyMethodProcessor(),this.xmlBodyMethodProcessor()));
//
//        return mappingHandlerAdapter;
//    }
//
//
////    @Bean
////    public CessPrefixMethodProcessor prefixMethodProcessor(){
////        return new CessPrefixMethodProcessor();
////    }
////
////    @Bean
////    public JsonBodyMethodProcessor jsonBodyMethodProcessor(){
////        return new JsonBodyMethodProcessor();
////    }
////
////    @Bean
////    public JsonMessageConverter jsonMessageConverter(){
////        JsonMessageConverter messageConverter = new JsonMessageConverter();
////        messageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
////
////        return messageConverter;
////    }
////
////
////    @Bean
////    public XmlBodyMethodProcessor xmlBodyMethodProcessor(){
////        return new XmlBodyMethodProcessor();
////    }
////
////    @Bean
////    public CessExceptionHandler exceptionHandler(){
////        return new CessExceptionHandler();
////    }
//
//}
