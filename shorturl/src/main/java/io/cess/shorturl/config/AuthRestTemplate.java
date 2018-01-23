//package io.cess.shorturl.config;
//
//import org.springframework.http.converter.ByteArrayHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.ResourceHttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
//import org.springframework.http.converter.xml.SourceHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AuthRestTemplate extends RestTemplate{
//
//    public AuthRestTemplate(){
//        super();
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//
//        messageConverters.add(new ByteArrayHttpMessageConverter());
//        messageConverters.add(new StringHttpMessageConverter());
//        messageConverters.add(new ResourceHttpMessageConverter(false));
//        messageConverters.add(new SourceHttpMessageConverter<>());
//        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
//
//        messageConverters.add(new MappingJackson2HttpMessageConverter());
//
//        messageConverters.add(new GsonHttpMessageConverter());
//
//        messageConverters.add(new MappingJackson2CborHttpMessageConverter());
//
//        this.setMessageConverters(messageConverters);
//    }
//}
