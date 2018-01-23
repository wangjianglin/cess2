package io.cess.cloud.config;

import io.cess.cloud.jwt.RsaJwtHelper;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


public class RsaJwtAccessTokenConverter extends AbstractJwtAccessTokenConverter{

    @Override
    protected Jwt decodeJwt(String token) {
        try {
            return RsaJwtHelper.decode(token, (RSAPublicKey) this.keyPair.getPublic(),(RSAPrivateKey)this.keyPair.getPrivate());
        } catch (Throwable e) {
            throw new IllegalArgumentException("Cannot convert access token to JSON", e);
        }
    }

    @Override
    protected Jwt encodeJwt(String content) {
        try {
            return RsaJwtHelper.encode(content, (RSAPrivateKey) this.keyPair.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
//public class RsaJwtAccessTokenConverter extends JwtAccessTokenConverter {
//
//    public static String OPEN_ID = "openid";
//
//    private RsaSigner signer;
//
//    private String openidKey;
//
//    private PrivateKey privateKey;
//    private RSAPublicKey publicKey;
//
//    public void setKeyPair(KeyPair keyPair) {
//        super.setKeyPair(keyPair);
////        privateKey = keyPair.getPrivate();
//////        signer = new RsaSigner((RSAPrivateKey) privateKey);
////        publicKey = (RSAPublicKey) keyPair.getPublic();
////        verifier = new RsaVerifier(publicKey);
////        verifierKey = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.getEncoder().encode(publicKey.getEncoded()))
////                + "\n-----END PUBLIC KEY-----";
//    }
//
////    private String encrypt(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
////        Cipher cipher = Cipher.getInstance("RSA");
////        // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
////        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
////        byte[] output = cipher.doFinal(str.getBytes());
////        return new String(Base64.getEncoder().encode(output));
////    }
////
////    private String deccrypt(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
////        Cipher cipher = Cipher.getInstance("RSA");
////        // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
////        cipher.init(Cipher.DECRYPT_MODE, publicKey);
////        byte[] output = cipher.doFinal(Base64.getDecoder().decode(str.getBytes()));
////        return new String(output);
////    }
//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        OAuth2AccessToken token = super.enhance(accessToken, authentication);
//
//        token.getAdditionalInformation().put(OPEN_ID,
//                Util.openId(this.openidKey,authentication.getName(),authentication.getOAuth2Request().getClientId()));
//
//        return token;
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
//        KeyPair keyPair = Rsa.generateKeyPair(2048);
//
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//
//        String token = RsaJwtHelper.encode("{\"aud\":[\"FOO\"],\"user_name\":\"1\",\"scope\":[\"GOODS\"],\"exp\":1515836555,\"authorities\":[\"goods:select\"],\"jti\":\"43bafa60-8e86-468c-90e4-7e697769f432\",\"client_id\":\"web_app\"}", privateKey).getEncoded();
//
//        System.out.println(token);
//        Jwt jwt = RsaJwtHelper.decode(token,publicKey,privateKey);
//
//        System.out.println(jwt.getClaims());
//    }
//}
