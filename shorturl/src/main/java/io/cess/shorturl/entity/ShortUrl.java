package io.cess.shorturl.entity;

import org.springframework.data.jpa.repository.Lock;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity(name = "shorturl_mapping")
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "delete_status",columnDefinition = "int default 0")
    private boolean deleteStatus = false;// 是否删除,默认为0未删除，-1表示删除状态

    @Column(name="short_url",length = 16)
    private String shortUrl;

    @Column(name = "long_url",length = 2048,unique = true)
    private String longUrl;

    @Temporal(TIMESTAMP)
    private Date createDate;

    @Column(length = 128)
    private String creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
