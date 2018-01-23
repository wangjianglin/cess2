package io.cess.core.spring;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class CessAbstractJaxb2HttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    private final ConcurrentMap<HandlerMethod, JAXBContext> jaxbContexts = new ConcurrentHashMap<>(64);

    protected CessAbstractJaxb2HttpMessageConverter() {
    }


    protected CessAbstractJaxb2HttpMessageConverter(MediaType supportedMediaType) {
        super(supportedMediaType);
    }

    /**
     * Construct an {@code AbstractHttpMessageConverter} with multiple supported media types.
     * @param supportedMediaTypes the supported media types
     */
    protected CessAbstractJaxb2HttpMessageConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
    }

    /**
     * Construct an {@code AbstractHttpMessageConverter} with a default charset and
     * multiple supported media types.
     * @param defaultCharset the default character set
     * @param supportedMediaTypes the supported media types
     * @since 4.3
     */
    protected CessAbstractJaxb2HttpMessageConverter(Charset defaultCharset, MediaType... supportedMediaTypes) {
//        this.defaultCharset = defaultCharset;
        super(defaultCharset,supportedMediaTypes);
    }
    /**
     * Create a new {@link Marshaller} for the given class.
     * @param clazz the class to create the marshaller for
     * @return the {@code Marshaller}
     * @throws HttpMessageConversionException in case of JAXB errors
     */
    protected final Marshaller createMarshaller(Class<?> clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            customizeMarshaller(marshaller);
            return marshaller;
        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException(
                    "Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
        }
    }

    /**
     * Customize the {@link Marshaller} created by this
     * message converter before using it to write the object to the output.
     * @param marshaller the marshaller to customize
     * @since 4.0.3
     * @see #createMarshaller(Class)
     */
    protected void customizeMarshaller(Marshaller marshaller) {
    }

    /**
     * Create a new {@link Unmarshaller} for the given class.
     * @param clazz the class to create the unmarshaller for
     * @return the {@code Unmarshaller}
     * @throws HttpMessageConversionException in case of JAXB errors
     */
    protected final Unmarshaller createUnmarshaller(Class<?> clazz) throws JAXBException {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            customizeUnmarshaller(unmarshaller);
            return unmarshaller;
        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException(
                    "Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
        }
    }

    /**
     * Customize the {@link Unmarshaller} created by this
     * message converter before using it to read the object from the input.
     * @param unmarshaller the unmarshaller to customize
     * @since 4.0.3
     * @see #createUnmarshaller(Class)
     */
    protected void customizeUnmarshaller(Unmarshaller unmarshaller) {
    }

    /**
     * Return a {@link JAXBContext} for the given class.
     * @param clazz the class to return the context for
     * @return the {@code JAXBContext}
     * @throws HttpMessageConversionException in case of JAXB errors
     */
    protected final JAXBContext getJaxbContext(Class<?> clazz) {
        Assert.notNull(clazz, "'clazz' must not be null");
        HandlerMethod handlerMethod = CessContext.getHandlerMethod();
        JAXBContext jaxbContext = this.jaxbContexts.get(handlerMethod);
        if (jaxbContext == null) {
            try {

                List<Class<?>> cls = new ArrayList<>();

                cls.add(Utils.ResultObj01.class);
                cls.add(Utils.ResultObj02.class);
                cls.add(clazz);

                XmlCls xmlCls = handlerMethod.getMethod().getAnnotation(XmlCls.class);
                if(xmlCls != null && xmlCls.cls() != null){
                    cls.addAll(Arrays.asList(xmlCls.cls()));
                }

                xmlCls = handlerMethod.getReturnType().getMethodAnnotation(XmlCls.class);

                if(xmlCls != null && xmlCls.cls() != null){
                    cls.addAll(Arrays.asList(xmlCls.cls()));
                }

                jaxbContext = JAXBContext.newInstance(cls.toArray(new Class[]{}));
                this.jaxbContexts.putIfAbsent(handlerMethod, jaxbContext);
            }
            catch (JAXBException ex) {
                throw new HttpMessageConversionException(
                        "Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }

}