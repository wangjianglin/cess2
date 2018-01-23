package io.cess.cloud.config;

import io.cess.cloud.oauth2.OAuth2Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.util.Assert;

import java.security.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractJwtAccessTokenConverter extends JwtAccessTokenConverter {

    public static String OPEN_ID = "openid";

    /**
     * Field name for token id.
     */
    public static final String TOKEN_ID = AccessTokenConverter.JTI;

    /**
     * Field name for access token id.
     */
    public static final String ACCESS_TOKEN_ID = AccessTokenConverter.ATI;

    private static final Log logger = LogFactory.getLog(JwtAccessTokenConverter.class);

    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    private JwtClaimsSetVerifier jwtClaimsSetVerifier = new NoOpJwtClaimsSetVerifier();

    private JsonParser objectMapper = JsonParserFactory.create();

    protected KeyPair keyPair;

    private String openidKey;

    private OAuth2Service authService;

    /**
     * @param tokenConverter the tokenConverter to set
     */
    public void setAccessTokenConverter(AccessTokenConverter tokenConverter) {
        this.tokenConverter = tokenConverter;
    }

    /**
     * @return the tokenConverter in use
     */
    public AccessTokenConverter getAccessTokenConverter() {
        return tokenConverter;
    }

    /**
     * @return the {@link JwtClaimsSetVerifier} used to verify the claim(s) in the JWT Claims Set
     */
    public JwtClaimsSetVerifier getJwtClaimsSetVerifier() {
        return this.jwtClaimsSetVerifier;
    }

    /**
     * @param jwtClaimsSetVerifier the {@link JwtClaimsSetVerifier} used to verify the claim(s) in the JWT Claims Set
     */
    public void setJwtClaimsSetVerifier(JwtClaimsSetVerifier jwtClaimsSetVerifier) {
        Assert.notNull(jwtClaimsSetVerifier, "jwtClaimsSetVerifier cannot be null");
        this.jwtClaimsSetVerifier = jwtClaimsSetVerifier;
    }

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        return tokenConverter.convertAccessToken(token, authentication);
    }

    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        return tokenConverter.extractAccessToken(value, map);
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        return tokenConverter.extractAuthentication(map);
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    abstract protected Jwt decodeJwt(String token);

    private OAuth2Authentication auth(OAuth2Authentication authentication){

        if(authService == null){
            return authentication;
        }
        Collection<GrantedAuthority> list = authService.authorityFilter(authentication.getAuthorities(),authentication.getOAuth2Request().getScope());
        return new OAuth2Authentication(authentication.getOAuth2Request(),authentication.getUserAuthentication()){

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return list;
            }
        };
    }
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        authentication = auth(authentication);

        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
        String tokenId = result.getValue();
        if (!info.containsKey(TOKEN_ID)) {
            info.put(TOKEN_ID, tokenId);
        }
        else {
            tokenId = (String) info.get(TOKEN_ID);
        }
        result.setAdditionalInformation(info);
        result.setValue(encode(result, authentication));
        OAuth2RefreshToken refreshToken = result.getRefreshToken();
        if (refreshToken != null) {
            DefaultOAuth2AccessToken encodedRefreshToken = new DefaultOAuth2AccessToken(accessToken);
            encodedRefreshToken.setValue(refreshToken.getValue());
            // Refresh tokens do not expire unless explicitly of the right type
            encodedRefreshToken.setExpiration(null);
            try {
//                Map<String, Object> claims = objectMapper
//                        .parseMap(EccJwtHelper.decode(refreshToken.getValue(),this.publicKey,this.privateKey).getClaims());
                Map<String, Object> claims = objectMapper
                        .parseMap(decodeJwt(refreshToken.getValue()).getClaims());
                if (claims.containsKey(TOKEN_ID)) {
                    encodedRefreshToken.setValue(claims.get(TOKEN_ID).toString());
                }
            }
            catch (IllegalArgumentException e) {
            }
            Map<String, Object> refreshTokenInfo = new LinkedHashMap<String, Object>(
                    accessToken.getAdditionalInformation());
            refreshTokenInfo.put(TOKEN_ID, encodedRefreshToken.getValue());
            refreshTokenInfo.put(ACCESS_TOKEN_ID, tokenId);
            encodedRefreshToken.setAdditionalInformation(refreshTokenInfo);
            DefaultOAuth2RefreshToken token = new DefaultOAuth2RefreshToken(
                    encode(encodedRefreshToken, authentication));
            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                Date expiration = ((ExpiringOAuth2RefreshToken) refreshToken).getExpiration();
                encodedRefreshToken.setExpiration(expiration);
                token = new DefaultExpiringOAuth2RefreshToken(encode(encodedRefreshToken, authentication), expiration);
            }
            result.setRefreshToken(token);
        }

        result.getAdditionalInformation().put(OPEN_ID,
                Util.openId(this.openidKey,authentication.getName(),authentication.getOAuth2Request().getClientId()));


        return result;
    }

    public boolean isRefreshToken(OAuth2AccessToken token) {
        return token.getAdditionalInformation().containsKey(ACCESS_TOKEN_ID);
    }

    abstract protected Jwt encodeJwt(String content);
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String content;
        try {
            content = objectMapper.formatMap(tokenConverter.convertAccessToken(accessToken, authentication));
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot convert access token to JSON", e);
        }
        return encodeJwt(content).getEncoded();
    }

    protected Map<String, Object> decode(String token) {
        try {
            token = token.replace(' ','+');
            Jwt jwt = decodeJwt(token);
            String claimsStr = jwt.getClaims();
            Map<String, Object> claims = objectMapper.parseMap(claimsStr);
            if (claims.containsKey(EXP) && claims.get(EXP) instanceof Integer) {
                Integer intValue = (Integer) claims.get(EXP);
                claims.put(EXP, new Long(intValue));
            }
            this.getJwtClaimsSetVerifier().verify(claims);
            return claims;
        }
        catch (Exception e) {
            throw new InvalidTokenException("Cannot convert access token to JSON", e);
        }
    }

    public void afterPropertiesSet() throws Exception {
//        if (verifier != null) {
//            // Assume signer also set independently if needed
//            return;
//        }
//        SignatureVerifier verifier = new MacSigner(verifierKey);
//        try {
//            verifier = new RsaVerifier(verifierKey);
//        }
//        catch (Exception e) {
//            logger.warn("Unable to create an RSA verifier from verifierKey (ignoreable if using MAC)");
//        }
//        // Check the signing and verification keys match
//        if (signer instanceof EccSigner) {
//            byte[] test = "test".getBytes();
//            try {
//                verifier.verify(test, signer.sign(test));
//                logger.info("Signing and verification RSA keys match");
//            }
//            catch (InvalidSignatureException e) {
//                logger.error("Signing and verification RSA keys do not match");
//            }
//        }
//        else if (verifier instanceof MacSigner) {
//            // Avoid a race condition where setters are called in the wrong order. Use of
//            // == is intentional.
//            Assert.state(this.signingKey == this.verifierKey,
//                    "For MAC signing you do not need to specify the verifier key separately, and if you do it must match the signing key");
//        }
//        this.verifier = verifier;
    }

    public void setAuthService(OAuth2Service authService) {
        this.authService = authService;
    }

    private class NoOpJwtClaimsSetVerifier implements JwtClaimsSetVerifier {
        @Override
        public void verify(Map<String, Object> claims) throws InvalidTokenException {
        }
    }

    public String getOpenidKey() {
        return openidKey;
    }

    public void setOpenidKey(String openidKey) {
        this.openidKey = openidKey;
    }

//    public static void main(String[] args)throws Throwable{
//        KeyPair keyPair = Ecc.generateKeyPair();
//
//        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
//        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
//
//        String token = EccJwtHelper.encode("{\"aud\":[\"FOO\"],\"user_name\":\"1\",\"scope\":[\"GOODS\"],\"exp\":1515836555,\"authorities\":[\"goods:select\"],\"jti\":\"43bafa60-8e86-468c-90e4-7e697769f432\",\"client_id\":\"web_app\"}", publicKey).getEncoded();
//
//        System.out.println(token);
//        Jwt jwt = EccJwtHelper.decode(token,publicKey,privateKey);
//
//        System.out.println(jwt.getClaims());
//    }
}
