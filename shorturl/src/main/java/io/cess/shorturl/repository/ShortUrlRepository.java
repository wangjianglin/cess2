package io.cess.shorturl.repository;

import io.cess.shorturl.entity.ShortUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface ShortUrlRepository extends CrudRepository<ShortUrl, Long> {

    ShortUrl findByShortUrl(String url);

    ShortUrl findByLongUrl(String url);
}
