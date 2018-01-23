package io.cess.core.spring;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

public class XmlMessageConverter extends CessAbstractJaxb2HttpMessageConverter<Object> {

    private boolean supportDtd = false;

    private boolean processExternalEntities = false;

    public final static Charset UTF8     = Charset.forName("UTF-8");

    private Charset             defaultCharset  = UTF8;

    private MediaType returnMediaType = new MediaType("application", "json",UTF8);

    public XmlMessageConverter(){
        super(new MediaType("application", "cess+xml",UTF8)
                ,new MediaType("application", "cess2+xml",UTF8));
    }

    public void setSupportDtd(boolean supportDtd) {
        this.supportDtd = supportDtd;
    }

    public boolean isSupportDtd() {
        return this.supportDtd;
    }

    /**
     * Indicates whether external XML entities are processed when converting to a Source.
     * <p>Default is {@code false}, meaning that external entities are not resolved.
     * <p><strong>Note:</strong> setting this option to {@code true} also
     * automatically sets {@link #setSupportDtd} to {@code true}.
     */
    public void setProcessExternalEntities(boolean processExternalEntities) {
        this.processExternalEntities = processExternalEntities;
        if (processExternalEntities) {
            setSupportDtd(true);
        }
    }

    /**
     * Returns the configured value for whether XML external entities are allowed.
     */
    public boolean isProcessExternalEntities() {
        return this.processExternalEntities;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException {

        outputMessage.getHeaders().setContentType(returnMediaType);

        Object result = Utils.writeMessage(o,outputMessage);

        Marshaller marshaller = super.createMarshaller(o.getClass());
        Charset charset = outputMessage.getHeaders().getContentType().getCharset();
        if(charset == null){
            charset = defaultCharset;
        }
        if(result == null){
            return;
        }
        try {
            marshaller.setProperty(Marshaller.JAXB_ENCODING,charset.name());
            marshaller.marshal(result,outputMessage.getBody());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
