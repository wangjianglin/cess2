package io.cess.shorturl;

import io.cess.core.spring.HtmlResourceView;
import io.cess.shorturl.entity.ShortUrl;
import io.cess.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;


@Controller
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@ApiIgnore
public class ShortUrlController {

    @Autowired
    private ShortUrlService service;

    @RequestMapping("/")
    @PreAuthorize("permitAll()")
    public String index(){
        return "index";
    }

    @RequestMapping("/docs.html")
    @PreAuthorize("permitAll()")
    public ModelAndView docs(){
        ModelAndView mv = new ModelAndView();
        mv.setView(new HtmlResourceView("/META-INF/resources/docs.html"));
        return mv;
    }


    @RequestMapping("/swagger-ui.html")
    @PreAuthorize("permitAll()")
    public ModelAndView swaggerui(){
        ModelAndView mv = new ModelAndView();
        mv.setView(new HtmlResourceView("/META-INF/resources/swagger-ui.html"));
        return mv;
    }

    private Pattern pattern = Pattern.compile("^((?![\\.|/|-]).)*$");
//    @RequestMapping("/{code:^((?![\\.|/|-]).)*$}")
    @RequestMapping("/{code}")
    @PreAuthorize("permitAll()")
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    public ModelAndView redirect(@PathVariable("code") String url){

        if(!pattern.matcher(url).matches()){
            return null;
        }

        long id = Utils.urlToId(url);

        ShortUrl shortUrl = service.getById(id);

        ModelAndView view = new ModelAndView();
        view.setView(new RedirectView(shortUrl.getLongUrl()));
        return view;
    }
}
