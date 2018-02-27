package io.cess.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AES {

    private enum KeySize{
        b128,b192,b256;

        int size(){
            switch (this){
                case b128:
                    return 128;
                case b192:
                    return 192;
                case b256:
                    return 256;
            }
            return 128;
        }
    }

    public static class B128 extends AES{

        public B128(String password) throws NoSuchAlgorithmException {
            super(KeySize.b128, password);
        }
    }
    public static class B192 extends AES{

        public B192(String password) throws NoSuchAlgorithmException {
            super(KeySize.b192, password);
        }
    }
    public static class B256 extends AES{

        public B256(String password) throws NoSuchAlgorithmException {
            super(KeySize.b256, password);
        }
    }

    private int keySize;
    private byte[] password;

    private AES(KeySize keySize,String password) throws NoSuchAlgorithmException {
        this.keySize = keySize.size();
        this.password = key(password).getEncoded();
    }

    private AES(KeySize keySize,byte[] password){
        this.keySize = keySize.size();
        this.password = password;
    }

    private SecretKey key(String password) throws NoSuchAlgorithmException {

        KeyGenerator kgen = KeyGenerator.getInstance("AES");

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());

        SecureRandom securerandom = new SecureRandom(password.getBytes());
        kgen.init(keySize,random);
        SecretKey secretKey = kgen.generateKey();

        return secretKey;
    }

    public byte[] encrypt(byte[] src) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        return encrypt(src,this.password);
    }

    public byte[] decrypt(byte[] src) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(src,this.password);
    }

    public String encrypt(String src) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(encrypt(src.getBytes()));
    }

    public String decrypt(String src) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        return new String(decrypt(Base64.getDecoder().decode(src)));
    }

    // 加密
    public static byte[] encrypt(byte[] sSrc,byte[] password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec skeySpec = new SecretKeySpec(password, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(sSrc);
    }
 
    // 解密
    public static byte[] decrypt(byte[] sSrc,byte[] password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec skeySpec = new SecretKeySpec(password, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(sSrc);
    }
}
