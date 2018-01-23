package io.cess.cloud.config;

import io.cess.cloud.jwt.EccJwtHelper;
import io.cess.util.Ecc;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;


public class EccJwtAccessTokenConverter extends AbstractJwtAccessTokenConverter{

    @Override
    protected Jwt decodeJwt(String token) {
        try {
            return EccJwtHelper.decode(token, (ECPublicKey) this.keyPair.getPublic(),(ECPrivateKey)this.keyPair.getPrivate());
        } catch (Throwable e) {
            throw new IllegalArgumentException("Cannot convert access token to JSON", e);
        }
    }

    @Override
    protected Jwt encodeJwt(String content) {
        try {
            return EccJwtHelper.encode(content, (ECPublicKey) this.keyPair.getPublic());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)throws Throwable{
        String str = "{\"aud\":[\"FOO\"],\"user_name\":\"1\",\"scope\":[\"GOODS\"],\"exp\":1516120136,\"authorities\":[\"ROLE_PROXY\",\"goods:select\"],\"jti\":\"5ceb7644-2d1c-481d-95fe-4b2f31b8fbaf\",\"client_id\":\"web_app\"}";

        EccJwtAccessTokenConverter tokenConverter = new EccJwtAccessTokenConverter();

        tokenConverter.setKeyPair(Ecc.generateKeyPair());

        String token = tokenConverter.encodeJwt(str).getEncoded();

        System.out.println("token:"+token);

        token = tokenConverter.decodeJwt(token).getClaims();

        System.out.println("token:"+token);
    }
}
//public class EccJwtAccessTokenConverter extends JwtAccessTokenConverter {
//
//    public static String OPEN_ID = "openid";
//
//    /**
//     * Field name for token id.
//     */
//    public static final String TOKEN_ID = AccessTokenConverter.JTI;
//
//    /**
//     * Field name for access token id.
//     */
//    public static final String ACCESS_TOKEN_ID = AccessTokenConverter.ATI;
//
//    private static final Log logger = LogFactory.getLog(JwtAccessTokenConverter.class);
//
//    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
//
//    private JwtClaimsSetVerifier jwtClaimsSetVerifier = new NoOpJwtClaimsSetVerifier();
//
//    private JsonParser objectMapper = JsonParserFactory.create();
//
////    private String verifierKey = new RandomValueStringGenerator().generate();
////
////    private Signer signer = new MacSigner(verifierKey);
////
////    private String signingKey = verifierKey;
////
////    private SignatureVerifier verifier;
//    private ECPublicKey publicKey;
//    private ECPrivateKey privateKey;
//
//    private String openidKey;
//
//    /**
//     * @param tokenConverter the tokenConverter to set
//     */
//    public void setAccessTokenConverter(AccessTokenConverter tokenConverter) {
//        this.tokenConverter = tokenConverter;
//    }
//
//    /**
//     * @return the tokenConverter in use
//     */
//    public AccessTokenConverter getAccessTokenConverter() {
//        return tokenConverter;
//    }
//
//    /**
//     * @return the {@link JwtClaimsSetVerifier} used to verify the claim(s) in the JWT Claims Set
//     */
//    public JwtClaimsSetVerifier getJwtClaimsSetVerifier() {
//        return this.jwtClaimsSetVerifier;
//    }
//
//    /**
//     * @param jwtClaimsSetVerifier the {@link JwtClaimsSetVerifier} used to verify the claim(s) in the JWT Claims Set
//     */
//    public void setJwtClaimsSetVerifier(JwtClaimsSetVerifier jwtClaimsSetVerifier) {
//        Assert.notNull(jwtClaimsSetVerifier, "jwtClaimsSetVerifier cannot be null");
//        this.jwtClaimsSetVerifier = jwtClaimsSetVerifier;
//    }
//
//    @Override
//    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//        return tokenConverter.convertAccessToken(token, authentication);
//    }
//
//    @Override
//    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
//        return tokenConverter.extractAccessToken(value, map);
//    }
//
//    @Override
//    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
//        return tokenConverter.extractAuthentication(map);
//    }
//
//    /**
//     * Unconditionally set the verifier (the verifer key is then ignored).
//     *
//     * @param verifier the value to use
//     */
////    public void setVerifier(SignatureVerifier verifier) {
////        this.verifier = verifier;
////    }
//
//    /**
//     * Unconditionally set the signer to use (if needed). The signer key is then ignored.
//     *
//     * @param signer the value to use
//     */
////    public void setSigner(Signer signer) {
////        this.signer = signer;
////    }
//
//    /**
//     * Get the verification key for the token signatures.
//     *
//     * @return the key used to verify tokens
//     */
////    public Map<String, String> getKey() {
////        Map<String, String> result = new LinkedHashMap<String, String>();
////        result.put("alg", signer.algorithm());
////        result.put("value", verifierKey);
////        return result;
////    }
//
//    public void setKeyPair(KeyPair keyPair) {
//        this.publicKey = (ECPublicKey) keyPair.getPublic();
//        this.privateKey = (ECPrivateKey) keyPair.getPrivate();
////        PrivateKey privateKey = keyPair.getPrivate();
////        Assert.state(privateKey instanceof ECPrivateKey, "KeyPair must be an ECC ");
////        signer = new EccSigner((ECPrivateKey) privateKey);
////        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
////        verifier = new EccVerifier(publicKey);
////        verifierKey = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.getEncoder().encode(publicKey.getEncoded()))
////                + "\n-----END PUBLIC KEY-----";
//    }
//
//    /**
//     * Sets the JWT signing key. It can be either a simple MAC key or an RSA key. RSA keys
//     * should be in OpenSSH format, as produced by <tt>ssh-keygen</tt>.
//     *
//     * @param key the key to be used for signing JWTs.
//     */
////    public void setSigningKey(String key) {
////        Assert.hasText(key);
////        key = key.trim();
////
////        this.signingKey = key;
////
////        if (isPublic(key)) {
////            signer = new EccSigner(key);
////            logger.info("Configured with RSA signing key");
////        }
////        else {
////            // Assume it's a MAC key
////            this.verifierKey = key;
////            signer = new MacSigner(key);
////        }
////    }
//
////    /**
////     * @return true if the key has a public verifier
////     */
////    private boolean isPublic(String key) {
////        return key.startsWith("-----BEGIN");
////    }
////
////    /**
////     * @return true if the signing key is a public key
////     */
////    public boolean isPublic() {
////        return signer instanceof RsaSigner;
////    }
//
//    /**
//     * The key used for verifying signatures produced by this class. This is not used but
//     * is returned from the endpoint to allow resource servers to obtain the key.
//     *
//     * For an HMAC key it will be the same value as the signing key and does not need to
//     * be set. For and RSA key, it should be set to the String representation of the
//     * public key, in a standard format (e.g. OpenSSH keys)
//     *
//     * paramkey the signature verification key (typically an RSA public key)
//     */
////    public void setVerifierKey(String key) {
////        this.verifierKey = key;
////    }
//
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
//        Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
//        String tokenId = result.getValue();
//        if (!info.containsKey(TOKEN_ID)) {
//            info.put(TOKEN_ID, tokenId);
//        }
//        else {
//            tokenId = (String) info.get(TOKEN_ID);
//        }
//        result.setAdditionalInformation(info);
//        result.setValue(encode(result, authentication));
//        OAuth2RefreshToken refreshToken = result.getRefreshToken();
//        if (refreshToken != null) {
//            DefaultOAuth2AccessToken encodedRefreshToken = new DefaultOAuth2AccessToken(accessToken);
//            encodedRefreshToken.setValue(refreshToken.getValue());
//            // Refresh tokens do not expire unless explicitly of the right type
//            encodedRefreshToken.setExpiration(null);
//            try {
//                Map<String, Object> claims = objectMapper
//                        .parseMap(EccJwtHelper.decode(refreshToken.getValue(),this.publicKey,this.privateKey).getClaims());
//                if (claims.containsKey(TOKEN_ID)) {
//                    encodedRefreshToken.setValue(claims.get(TOKEN_ID).toString());
//                }
//            }
//            catch (IllegalArgumentException e) {
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (InvalidAlgorithmParameterException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (NoSuchProviderException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            Map<String, Object> refreshTokenInfo = new LinkedHashMap<String, Object>(
//                    accessToken.getAdditionalInformation());
//            refreshTokenInfo.put(TOKEN_ID, encodedRefreshToken.getValue());
//            refreshTokenInfo.put(ACCESS_TOKEN_ID, tokenId);
//            encodedRefreshToken.setAdditionalInformation(refreshTokenInfo);
//            DefaultOAuth2RefreshToken token = new DefaultOAuth2RefreshToken(
//                    encode(encodedRefreshToken, authentication));
//            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//                Date expiration = ((ExpiringOAuth2RefreshToken) refreshToken).getExpiration();
//                encodedRefreshToken.setExpiration(expiration);
//                token = new DefaultExpiringOAuth2RefreshToken(encode(encodedRefreshToken, authentication), expiration);
//            }
//            result.setRefreshToken(token);
//        }
//
//        result.getAdditionalInformation().put(OPEN_ID,
//                Util.openId(this.openidKey,authentication.getName(),authentication.getOAuth2Request().getClientId()));
//
//
//        return result;
//    }
//
//    public boolean isRefreshToken(OAuth2AccessToken token) {
//        return token.getAdditionalInformation().containsKey(ACCESS_TOKEN_ID);
//    }
//
//    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        String content;
//        try {
//            content = objectMapper.formatMap(tokenConverter.convertAccessToken(accessToken, authentication));
//        }
//        catch (Exception e) {
//            throw new IllegalStateException("Cannot convert access token to JSON", e);
//        }
//        String token = null;
//        try {
//            token = EccJwtHelper.encode(content, this.publicKey).getEncoded();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return token;
//    }
//
//    protected Map<String, Object> decode(String token) {
//        try {
//            Jwt jwt = EccJwtHelper.decode(token, this.publicKey,this.privateKey);
//            String claimsStr = jwt.getClaims();
//            Map<String, Object> claims = objectMapper.parseMap(claimsStr);
//            if (claims.containsKey(EXP) && claims.get(EXP) instanceof Integer) {
//                Integer intValue = (Integer) claims.get(EXP);
//                claims.put(EXP, new Long(intValue));
//            }
//            this.getJwtClaimsSetVerifier().verify(claims);
//            return claims;
//        }
//        catch (Exception e) {
//            throw new InvalidTokenException("Cannot convert access token to JSON", e);
//        }
//    }
//
//    public void afterPropertiesSet() throws Exception {
////        if (verifier != null) {
////            // Assume signer also set independently if needed
////            return;
////        }
////        SignatureVerifier verifier = new MacSigner(verifierKey);
////        try {
////            verifier = new RsaVerifier(verifierKey);
////        }
////        catch (Exception e) {
////            logger.warn("Unable to create an RSA verifier from verifierKey (ignoreable if using MAC)");
////        }
////        // Check the signing and verification keys match
////        if (signer instanceof EccSigner) {
////            byte[] test = "test".getBytes();
////            try {
////                verifier.verify(test, signer.sign(test));
////                logger.info("Signing and verification RSA keys match");
////            }
////            catch (InvalidSignatureException e) {
////                logger.error("Signing and verification RSA keys do not match");
////            }
////        }
////        else if (verifier instanceof MacSigner) {
////            // Avoid a race condition where setters are called in the wrong order. Use of
////            // == is intentional.
////            Assert.state(this.signingKey == this.verifierKey,
////                    "For MAC signing you do not need to specify the verifier key separately, and if you do it must match the signing key");
////        }
////        this.verifier = verifier;
//    }
//
//    private class NoOpJwtClaimsSetVerifier implements JwtClaimsSetVerifier {
//        @Override
//        public void verify(Map<String, Object> claims) throws InvalidTokenException {
//        }
//    }
//
//    public String getOpenidKey() {
//        return openidKey;
//    }
//
//    public void setOpenidKey(String openidKey) {
//        this.openidKey = openidKey;
//    }
//
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
//}
