package io.cess.shorturl.api;


import io.cess.core.spring.CessBody;
import io.cess.shorturl.entity.ShortUrl;
import io.cess.shorturl.service.ShortUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

@Api(value = "Sample-API", description = "测试")
@RestController
@RequestMapping({"api"})
public class ApiControler {

    @Value("${io.cess.shorturl.pre-url:}")
    private String preUrl;

    @Autowired
    private ShortUrlService service;

    @ApiOperation(value = "获取用户列表",notes = "根据url的id来获取用户详细信息，返回List<User>类型用户信息的JSON")
    @RequestMapping("/a")
    @CessBody
    public String index() {
        return "ok.";
    }

    @RequestMapping("/create")
    @CessBody
    public ShortUrl create(Principal principal,String url) {
        ShortUrl shortUrl = service.create(url,principal.getName());
        process(shortUrl);
        return shortUrl;
    }

    @RequestMapping("/query/shorturl")
    @CessBody
    public ShortUrl findByLongUrl(String url){
        ShortUrl shortUrl = service.findByLongUrl(url);
        process(shortUrl);
        return shortUrl;
    }

    @PreAuthorize("#oauth2.isOAuth()")
    @RequestMapping("/query/longurl")
    @CessBody
    public ShortUrl findByShortUrl(String url){
        ShortUrl shortUrl = service.findByShortUrl(url);
        process(shortUrl);
        return shortUrl;
    }

    private void process(ShortUrl shortUrl){
        if(preUrl == null || !preUrl.endsWith("/")){
            shortUrl.setShortUrl(preUrl + "/" + shortUrl.getShortUrl());
        }else{
            shortUrl.setShortUrl((preUrl + shortUrl.getShortUrl()));
        }
    }
}
