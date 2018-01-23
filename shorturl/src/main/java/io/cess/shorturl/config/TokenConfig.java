//package io.cess.shorturl.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.security.jwt.crypto.sign.RsaVerifier;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.util.FileCopyUtils;
//
//import java.io.IOException;
//
//@Configuration
//@ConditionalOnProperty(value = "io.cess.auth.mode")
//public class TokenConfig {
//
//    @Value("${io.cess.auth.pulick-key:}")
//    private String publicKey;
//    @Bean
//    @ConditionalOnProperty(value = "io.cess.auth.mode",havingValue = "jwt")
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(jwtTokenEnhancer());
//    }
//
//    protected JwtAccessTokenConverter jwtTokenEnhancer() {
//        JwtAccessTokenConverter converter =  new JwtAccessTokenConverter();
//        String key = "-----BEGIN PUBLIC KEY-----\n"+publicKey+"\n-----END PUBLIC KEY-----";
//        converter.setVerifierKey(key);
//        converter.setVerifier(new RsaVerifier(key));
//        return converter;
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = "io.cess.auth.mode",havingValue = "remote")
//    @ConfigurationProperties("security.oauth2.client")
//    public RemoteTokenServices tokenServices(){
//        RemoteTokenServices tokenServices = new RemoteTokenServices();
//        tokenServices.setRestTemplate(new AuthRestTemplate());
//
//        return tokenServices;
//    }
//}
