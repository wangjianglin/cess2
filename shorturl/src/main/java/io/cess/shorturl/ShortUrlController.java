package io.cess.shorturl;

import io.cess.shorturl.entity.ShortUrl;
import io.cess.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class ShortUrlController {

    @Autowired
    private ShortUrlService service;

    @RequestMapping("/")
    @PreAuthorize("permitAll()")
    public String index(){
        return "index";
    }

    @RequestMapping("/{code}")
    @PreAuthorize("permitAll()")
    public ModelAndView redirect(@PathVariable("code") String url){

        long id = Utils.urlToId(url);

        ShortUrl shortUrl = service.getById(id);

        ModelAndView view = new ModelAndView();
        view.setView(new RedirectView(shortUrl.getLongUrl()));
        return view;
    }
}
