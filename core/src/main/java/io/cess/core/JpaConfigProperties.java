package io.cess.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by lin on 27/10/2016.
 */
//@ConfigurationProperties(prefix="jpa",locations = "classpath:application.properties")
public class JpaConfigProperties {

    private String unitName;
    private String url;

    public String getUnitName() {
        return unitName;
    }

    public JpaConfigProperties(){
        System.out.println("========");
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
