package io.cess.shorturl.service;

import io.cess.shorturl.entity.ShortUrl;

public interface ShortUrlService {

//    ShortUrl create()

    ShortUrl getById(long id);

    ShortUrl create(String url,String creator);

    ShortUrl findByShortUrl(String url);

    ShortUrl findByLongUrl(String url);
}
