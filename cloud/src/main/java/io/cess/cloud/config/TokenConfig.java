package io.cess.cloud.config;

import io.cess.cloud.JsonRestTemplate;
import io.cess.cloud.Utils;
import io.cess.cloud.oauth2.AuthorityDecisionService;
import io.cess.cloud.oauth2.OAuth2Service;
import io.cess.util.Ecc;
import io.cess.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@ConditionalOnProperty(value = "io.cess.auth.mode")
public class TokenConfig {

    @Value("${io.cess.auth.public-key:#{null}}")
    private String publicKeyStr;

    @Value("${io.cess.auth.resource-jwt-key-uri:#{null}}")
    private String publicKeyUrl;

    @Value("${io.cess.auth.private-key:#{null}}")
    private String privateKeyStr;

    @Value("${io.cess.auth.encrypt-algorithm:RSA}")
    private String encryptAlgorithm;

    @Value("${io.cess.auth.openid-key:}")
    private String openidKey;

    @Autowired(required = false)
    private OAuth2Service authService;

    @Bean
    @ConditionalOnProperty(value = "io.cess.auth.mode")
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws Exception {
        AbstractJwtAccessTokenConverter converter = null;
        if(encryptAlgorithm != null && "ecc".equals(encryptAlgorithm.toLowerCase())){
            converter = jwtECCTokenEnhancer();
        }else{
            converter = jwtRSATokenEnhancer();
        }
        converter.setAuthService(authService);
//        if(authorityDecisionService != null) {
//            converter.setAccessTokenConverter(new AccessTokenConverter() {
//                @Override
//                public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//                    return authorityDecisionService.convertAccessToken(token,authentication);
//                }
//
//                @Override
//                public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
//                    return authorityDecisionService.extractAccessToken(value, map);
//                }
//
//                @Override
//                public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
//                    return authorityDecisionService.extractAuthentication(map);
//                }
//            });
//        }

        return converter;
    }

    private AbstractJwtAccessTokenConverter jwtECCTokenEnhancer() throws Exception{
        EccJwtAccessTokenConverter converter =  new EccJwtAccessTokenConverter();

        converter.setVerifierKey(processPublicKeyStr());

//        ECPublicKey publicKey = Ecc.getPublicKey(publicKeyStr);
        ECPrivateKey privateKey = Ecc.getPrivateKey(publicKeyStr);

//        if(publicKey != null){
//            converter.setVerifier(new EccVerifier(publicKey));
//        }

        ECPublicKey publicKey = null;
        if(privateKeyStr != null && !"".equals(privateKeyStr)){
            publicKey = Ecc.getPublicKey(privateKeyStr);
        }

//        if(privateKey != null){
            converter.setKeyPair(new KeyPair(publicKey,privateKey));
//        }

        converter.setOpenidKey(this.openidKey);

        return converter;
    }

    private String processPublicKeyStr(){
        String completionPublicKeyStr = "-----BEGIN PUBLIC KEY-----\n"+publicKeyStr+"\n-----END PUBLIC KEY-----";

        if((publicKeyStr == null || "".equals(publicKeyStr))
                && publicKeyUrl != null && !"".equals(publicKeyUrl)){

            RestTemplate restTemplate = new JsonRestTemplate();
            PublicKeyResult keyStr = restTemplate.getForObject(publicKeyUrl,PublicKeyResult.class);

            completionPublicKeyStr = keyStr.value;

            Matcher matcher = Pattern.compile("-----BEGIN PUBLIC KEY-----\n(.*)\n-----END PUBLIC KEY-----").matcher(completionPublicKeyStr);

            matcher.matches();

            publicKeyStr = matcher.group(1);

        }
        return completionPublicKeyStr;
    }

    private AbstractJwtAccessTokenConverter jwtRSATokenEnhancer() throws Exception {
        RsaJwtAccessTokenConverter converter =  new RsaJwtAccessTokenConverter();

//        converter.setVerifierKey(processPublicKeyStr());

        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey(publicKeyStr);

//        if(publicKey != null){
//            converter.setVerifier(new RsaVerifier(publicKey));
//        }

        RSAPrivateKey privateKey = null;
        if(privateKeyStr != null && !"".equals(privateKeyStr)){
            privateKey = (RSAPrivateKey) getPrivateKey(privateKeyStr);
        }


//        if(privateKey != null){
            converter.setKeyPair(new KeyPair(publicKey,privateKey));
//        }
        converter.setOpenidKey(this.openidKey);
        return converter;
    }

    private PublicKey getPublicKey(String key) throws Exception {
         byte[] keyBytes = Base64.getDecoder().decode(key);
         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         PublicKey publicKey = keyFactory.generatePublic(keySpec);
         return publicKey;
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
         byte[] keyBytes = Base64.getDecoder().decode(key);
         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
         return privateKey;
    }

    @Bean
    @ConditionalOnProperty(value = "io.cess.auth.mode",havingValue = "remote")
    @ConfigurationProperties("security.oauth2.client")
    public RemoteTokenServices tokenServices(){
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        JsonRestTemplate restTemplate = new JsonRestTemplate();
        tokenServices.setRestTemplate(restTemplate);


        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {


                HttpStatus statusCode = response.getStatusCode();
                String str = Utils.toString(response);

                HttpError error = JsonUtil.deserialize(str,HttpError.class);

                if("invalid_token".equals(error.error)){
                    throw new InvalidTokenException(error.error_description);
                }

                throw new OAuth2Exception(str){
                    @Override
                    public int getHttpErrorCode() {
                        return statusCode.value();
                    }
                };
            }

        });

        return tokenServices;
    }

    public static class HttpError{
        private String error;
        private String error_description;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError_description() {
            return error_description;
        }

        public void setError_description(String error_description) {
            this.error_description = error_description;
        }
    }

    public static class PublicKeyResult{
        private String alg;
        private String value;

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
