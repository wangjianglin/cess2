package io.cess.core.spring;

import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HtmlResourceView implements View

{

    private String resource;
    private String contentType = MediaType.TEXT_HTML_VALUE;
    public HtmlResourceView(String resource,String contentType) {
        this.resource = resource;
        if(contentType != null && !"".equals(contentType.trim())) {
            this.contentType = contentType.trim();
        }
    }

    public HtmlResourceView(String resource) {
        this(resource,null);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        FileCopyUtils.copy(this.getClass().getResourceAsStream(this.resource),
                response.getOutputStream());
    }
}
