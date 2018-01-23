package io.cess.util;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import javax.crypto.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;

public class Ecc {
    private static final String ALGORITHM = "ECIES";
    private static final String PROVIDER = "BC";


    public static enum Curve{
        secp160k1("secp160k1"),
        secp160r1("secp160r1"),
        secp160r2("secp160r2"),
        secp192k1("secp192k1"),
        secp192r1("secp192r1"),
        secp224k1("secp224k1"),
        secp224r1("secp224r1"),
        secp256k1("secp256k1"),
        secp256r1("secp256r1"),
        secp384r1("secp384r1"),
        secp521r1("secp521r1"),
        sect113r1("sect113r1"),
        sect113r2("sect113r2"),
        sect131r1("sect131r1"),
        sect131r2("sect131r2"),
        sect163k1("sect163k1"),
        sect163r1("sect163r1"),
        sect163r2("sect163r2"),
        sect193r1("sect193r1"),
        sect193r2("sect193r2"),
        sect233k1("sect233k1"),
        sect233r1("sect233r1"),
        sect239k1("sect239k1"),
        sect283k1("sect283k1"),
        sect283r1("sect283r1"),
        sect409k1("sect409k1"),
        sect409r1("sect409r1"),
        sect571k1("sect571k1"),
        sect571r1("sect571r1"),
        sm2p256v1("sm2p256v1");

        private String val;
        Curve(String val){
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }

    public static ECPrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC",PROVIDER);

        ECPrivateKey priKey = (ECPrivateKey) keyFactory
                .generatePrivate(pkcs8KeySpec);

        return priKey;
    }

    public static ECPublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC",PROVIDER);;

        return (ECPublicKey) keyFactory
                .generatePublic(x509KeySpec);
    }

    public static String decrypt(String data, ECPrivateKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return Base64.getEncoder().encodeToString(decrypt(Base64.getDecoder().decode(data),key));
    }

    public static byte[] decrypt(byte[] data, ECPrivateKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {

        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(key.getS(),
                key.getParams());

        // 对数据解密
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public static String decrypt(String data, ECPublicKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return Base64.getEncoder().encodeToString(decrypt(Base64.getDecoder().decode(data),key));
    }

    public static byte[] decrypt(byte[] data, ECPublicKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {

        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(key.getW(),
                key.getParams());

        // 对数据解密
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, key, ecPublicKeySpec.getParams());

        return cipher.doFinal(data);
    }

    public static String encrypt(String data,ECPublicKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return Base64.getEncoder().encodeToString(encrypt(data.getBytes(),key));
    }

    public static byte[] encrypt(byte[] data,ECPublicKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
//        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(key.getW(),
//                key.getParams());

        // 对数据加密
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public static String encrypt(String data,ECPrivateKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return Base64.getEncoder().encodeToString(encrypt(data.getBytes(),key));
    }

    public static byte[] encrypt(byte[] data,ECPrivateKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
//        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(key.getS(),
//                key.getParams());

        // 对数据加密
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    /**
     * 取得公钥
     *
     * @param
     * @return
     * @throws Exception
     */
    public static String getKeyString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return generateKeyPair(Curve.sect571r1);
    }

    public static KeyPair generateKeyPair(Curve curve) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
        ECNamedCurveParameterSpec parameterSpec =
                ECNamedCurveTable.getParameterSpec(curve.val);
        kpg.initialize(parameterSpec, new SecureRandom());

        return kpg.generateKeyPair();
    }


    private static void signate(ECPublicKey pubKey,ECPrivateKey privKey)throws Exception{
        Signature ecdsa;
        ecdsa = Signature.getInstance("SHA1withECDSA","BC");
        ecdsa.initSign(privKey);

        String text = "In teaching others we teach ourselves";
        System.out.println("Text: " + text);
        byte[] baText = text.getBytes("UTF-8");

        ecdsa.update(baText);
        byte[] baSignature = ecdsa.sign();
        System.out.println("Signature: 0x" + (new BigInteger(1, baSignature).toString(16)).toUpperCase());

        Signature signature;
        signature = Signature.getInstance("SHA1withECDSA","BC");
        signature.initVerify(pubKey);
        baText[0] = 1;
        signature.update(baText);
        boolean result = signature.verify(baSignature);
        System.out.println("Valid: " + result);
    }

    public static void main(String[] args) throws Exception {

        String inputStr = "3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app";
        byte[] data = inputStr.getBytes();

//        Map<String, Object> keyMap = initKey();

        KeyPair keyPair = generateKeyPair();

        String publicKey = getKeyString(keyPair.getPublic());
        String privateKey = getKeyString(keyPair.getPrivate());
        System.err.println("公钥: \n" + publicKey);
        System.err.println("私钥： \n" + privateKey);

        ECPrivateKey priKey = getPrivateKey(privateKey);

        byte[] encodedData = encrypt(data, (ECPublicKey) keyPair.getPublic());


        System.out.println(encodedData.length);
        System.out.println(new String(Base64.getEncoder().encode(encodedData)));

        byte[] decodedData = decrypt(encodedData, priKey);


        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        signate((ECPublicKey) keyPair.getPublic(),priKey);

    }


    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}
