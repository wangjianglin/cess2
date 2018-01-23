package io.cess.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

public class ResourceRestTemplate extends RestTemplate {

    @Value("${io.cess.gzip:false}")
    private boolean gzip = false;
    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        final ClientHttpRequest request = super.createRequest(url, method);

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getHeaders().add("authorization", httpServletRequest.getHeader("authorization"));

        if(gzip){
            request.getHeaders().add("Accept-Encoding","gzip,deflate");
        }

        return request;
    }

    public boolean isGzip() {
        return gzip;
    }

    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }
}
