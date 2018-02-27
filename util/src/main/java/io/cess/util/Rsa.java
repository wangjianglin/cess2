package io.cess.util;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class Rsa {
    private static final String ALGORITHM = "RSA";
//    private static final String PROVIDER = "SunJSSE";



    public static RSAPrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

        RSAPrivateKey priKey = (RSAPrivateKey) keyFactory
                .generatePrivate(pkcs8KeySpec);

        return priKey;
    }

    public static RSAPublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);;

        return (RSAPublicKey) keyFactory
                .generatePublic(x509KeySpec);
    }

//    public static String decrypt(String data, RSAPrivateKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
//        return Base64.getEncoder().encodeToString(decrypt(Base64.getDecoder().decode(data),key));
//    }
//
//    public static byte[] decrypt(byte[] data, RSAPrivateKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
//
//        // 对数据解密
//        Cipher cipher = Cipher.getInstance(ALGORITHM,PROVIDER);
//        cipher.init(Cipher.DECRYPT_MODE, key);
//
//        return cipher.doFinal(data);
//    }

    public static String decrypt(String data, RSAKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
        return Base64.getEncoder().encodeToString(decrypt(Base64.getDecoder().decode(data),key));
    }

    public static byte[] decrypt(byte[] data, RSAKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        int decodeLen = key.getModulus().bitLength() / 8;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < data.length; i += decodeLen) {
            int end = i + decodeLen;
            if(end > data.length){
                end = data.length;
            }
            byte[] subarray = Arrays.copyOfRange(data, i, end);
            byte[] doFinal = decryptImpl(subarray, key);

            out.write(doFinal);
        }
        return out.toByteArray();
    }

    private static byte[] decryptImpl(byte[] data, RSAKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        // 对数据解密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, (Key) key);

        return cipher.doFinal(data);
    }

//    public static String encrypt(String data,RSAPublicKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
//        return Base64.getEncoder().encodeToString(encrypt(data.getBytes(),key));
//    }
//
//    public static byte[] encrypt(byte[] data,RSAPublicKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
//
//        // 对数据加密
//        Cipher cipher = Cipher.getInstance(ALGORITHM,PROVIDER);
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//
//        return cipher.doFinal(data);
//    }

    public static String encrypt(String data,RSAKey key) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
        return Base64.getEncoder().encodeToString(encrypt(data.getBytes(),key));
    }

    public static byte[] encrypt(byte[] data,RSAKey key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        int decodeLen = key.getModulus().bitLength() / 8 - 11;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < data.length; i += decodeLen) {
            int end = i + decodeLen;
            if(end > data.length){
                end = data.length;
            }
            byte[] subarray = Arrays.copyOfRange(data, i, end);
            byte[] doFinal = encryptImpl(subarray, (Key) key);
            out.write(doFinal);
        }
        return out.toByteArray();
    }

    private static byte[] encryptImpl(byte[] data,Key key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {

        // 对数据加密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
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

    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(keySize, new SecureRandom());

        return kpg.generateKeyPair();
    }


//    private static void signate(RSAPublicKey pubKey,RSAPrivateKey privKey)throws Exception{
//        Signature ecdsa;
//        ecdsa = Signature.getInstance("SHA1withECDSA","BC");
//        ecdsa.initSign(privKey);
//
//        String text = "In teaching others we teach ourselves";
//        System.out.println("Text: " + text);
//        byte[] baText = text.getBytes("UTF-8");
//
//        ecdsa.update(baText);
//        byte[] baSignature = ecdsa.sign();
//        System.out.println("Signature: 0x" + (new BigInteger(1, baSignature).toString(16)).toUpperCase());
//
//        Signature signature;
//        signature = Signature.getInstance("SHA1withECDSA","BC");
//        signature.initVerify(pubKey);
//        baText[0] = 1;
//        signature.update(baText);
//        boolean result = signature.verify(baSignature);
//        System.out.println("Valid: " + result);
//    }

    public static void main(String[] args) throws Exception {

        String inputStr = "3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app3789655456999:web_app37899:web_app3789655456999:web_app";
        byte[] data = inputStr.getBytes();

//        Map<String, Object> keyMap = initKey();

        KeyPair keyPair = generateKeyPair(2048);

        System.out.println(new Date().getTime());
        String publicKey = getKeyString(keyPair.getPublic());
        String privateKey = getKeyString(keyPair.getPrivate());
        System.err.println("公钥: \n" + publicKey);
        System.err.println("私钥： \n" + privateKey);

//        RSAPrivateKey priKey = getPrivateKey(privateKey);

//        byte[] encodedData = encrypt(data, (RSAPrivateKey) keyPair.getPrivate());
        byte[] encodedData = encrypt(data, (RSAPublicKey) keyPair.getPublic());


        System.out.println(encodedData.length);
        System.out.println(new String(Base64.getEncoder().encode(encodedData)));

        System.out.println(new Date().getTime());
        long s = new Date().getTime();

        int count = 100;
        byte[] decodedData = null;
        for(int n=0;n<count;n++) {
//            decodedData = decrypt(encodedData, (RSAPublicKey) keyPair.getPublic());
            decodedData = decrypt(encodedData, (RSAPrivateKey) keyPair.getPrivate());
        }
        System.out.println((new Date().getTime() - s)*1.0/count);

        String outputStr = new String(decodedData);
//        System.out.println(new Date().getTime());
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);


        //signate((RSAPublicKey) keyPair.getPublic(),priKey);

    }
}
