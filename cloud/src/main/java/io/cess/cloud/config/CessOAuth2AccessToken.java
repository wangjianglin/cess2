//package io.cess.cloud.config;
//
//import org.springframework.security.oauth2.common.*;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//
//import java.util.Date;
//import java.util.Map;
//import java.util.Set;
//
//
//@org.codehaus.jackson.map.annotate.JsonSerialize(using = OAuth2AccessTokenJackson1Serializer.class)
//@org.codehaus.jackson.map.annotate.JsonDeserialize(using = OAuth2AccessTokenJackson1Deserializer.class)
//@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CessOAuth2AccessTokenJackson2Serializer.class)
//@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OAuth2AccessTokenJackson2Deserializer.class)
//
//public class CessOAuth2AccessToken implements OAuth2AccessToken {
//
//    public static String OPEN_ID = "openid";
//
//    private OAuth2AccessToken token;
//    private OAuth2Authentication authentication;
//
//    public CessOAuth2AccessToken(OAuth2AccessToken token,OAuth2Authentication authentication){
//        this.token = token;
//        this.authentication = authentication;
//    }
//
//    @Override
//    public Map<String, Object> getAdditionalInformation() {
//        return token.getAdditionalInformation();
//    }
//
//    @Override
//    public Set<String> getScope() {
//        return token.getScope();
//    }
//
//    @Override
//    public OAuth2RefreshToken getRefreshToken() {
//        return token.getRefreshToken();
//    }
//
//    @Override
//    public String getTokenType() {
//        return token.getTokenType();
//    }
//
//    @Override
//    public boolean isExpired() {
//        return token.isExpired();
//    }
//
//    @Override
//    public Date getExpiration() {
//        return token.getExpiration();
//    }
//
//    @Override
//    public int getExpiresIn() {
//        return token.getExpiresIn();
//    }
//
//    @Override
//    public String getValue() {
//        return token.getValue();
//    }
//
//    public String getOpenId(){
//        return this.authentication.getName()+":"+authentication.getOAuth2Request().getClientId();
//    }
//}
