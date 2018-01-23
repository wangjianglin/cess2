package io.cess.shorturl.service.impl;

import io.cess.shorturl.Utils;
import io.cess.shorturl.entity.ShortUrl;
import io.cess.shorturl.repository.ShortUrlRepository;
import io.cess.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Component
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private ShortUrlRepository repository;

    @Override
    public ShortUrl getById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public ShortUrl create(String url, String creator) {

        ShortUrl shortUrl = repository.findByLongUrl(url);

        if(shortUrl != null){
            return shortUrl;
        }

        shortUrl = new ShortUrl();
        shortUrl.setCreateDate(new Date());
        shortUrl.setLongUrl(url);
        shortUrl.setCreator(creator);

        shortUrl = repository.save(shortUrl);

        shortUrl.setShortUrl(Utils.idToUrl(shortUrl.getId()));

        return shortUrl;
    }

    @Override
    public ShortUrl findByShortUrl(String url) {
        return repository.findByShortUrl(url);
    }

    @Override
    public ShortUrl findByLongUrl(String url) {
        return repository.findByLongUrl(url);
    }
}
