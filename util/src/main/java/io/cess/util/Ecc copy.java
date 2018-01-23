//package io.cess.util;
//
//import org.bouncycastle.asn1.sec.SECNamedCurves;
//import org.bouncycastle.asn1.x9.X9ECParameters;
//import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
//import org.bouncycastle.crypto.params.ECDomainParameters;
//import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
//import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
//import org.bouncycastle.crypto.params.ECPublicKeyParameters;
//import sun.security.ec.ECKeyFactory;
//import sun.security.ec.ECKeyPairGenerator;
//import sun.security.ec.ECPrivateKeyImpl;
//import sun.security.ec.ECPublicKeyImpl;
//
//import javax.crypto.*;
//import java.math.BigInteger;
//import java.security.*;
//import java.security.interfaces.ECPrivateKey;
//import java.security.interfaces.ECPublicKey;
//import java.security.spec.*;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.bouncycastle.asn1.sec.SECNamedCurves.*;
//
//public class Ecc {
//    public static final String ALGORITHM = "EC";
//
//    public static ECPrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());
//
//        // 取得私钥
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
//
//        ECPrivateKey priKey = (ECPrivateKey) keyFactory
//                .generatePrivate(pkcs8KeySpec);
//
//        return priKey;
//    }
//
//    public static ECPublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());
//
//        // 取得公钥
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);;
//
//        return (ECPublicKey) keyFactory
//                .generatePublic(x509KeySpec);
//    }
//    public static byte[] decrypt(byte[] data, ECPrivateKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//
//        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(key.getS(),
//                key.getParams());
//
//        // 对数据解密
//        // TODO Chipher不支持EC算法 未能实现
//        Cipher cipher = new NullCipher();
//        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
//        cipher.init(Cipher.DECRYPT_MODE, key, ecPrivateKeySpec.getParams());
//
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] decrypt(byte[] data, ECPublicKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//
//        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(key.getW(),
//                key.getParams());
//
//        // 对数据解密
//        // TODO Chipher不支持EC算法 未能实现
//        Cipher cipher = new NullCipher();
//        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
//        cipher.init(Cipher.DECRYPT_MODE, key, ecPublicKeySpec.getParams());
//
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] encrypt(byte[] data,ECPublicKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(key.getW(),
//                key.getParams());
//
//        // 对数据加密
//        // TODO Chipher不支持EC算法 未能实现
//        Cipher cipher = new NullCipher();
//        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
//        cipher.init(Cipher.ENCRYPT_MODE, key, ecPublicKeySpec.getParams());
//
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] encrypt(byte[] data,ECPrivateKey key) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(key.getS(),
//                key.getParams());
//
//        // 对数据加密
//        // TODO Chipher不支持EC算法 未能实现
//        Cipher cipher = new NullCipher();
//        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
//        cipher.init(Cipher.ENCRYPT_MODE, key, ecPrivateKeySpec.getParams());
//
//        return cipher.doFinal(data);
//    }
////    /**
////     * 取得私钥
////     *
////     * @param keyMap
////     * @return
////     * @throws Exception
////     */
////    public static String getPrivateKey(Map<String, Object> keyMap)
////            throws Exception {
////        Key key = (Key) keyMap.get(PRIVATE_KEY);
////
//////        return encryptBASE64(key.getEncoded());
////        return Base64.getEncoder().encodeToString(key.getEncoded());
////    }
//
//    /**
//     * 取得公钥
//     *
//     * @param
//     * @return
//     * @throws Exception
//     */
//    public static String getKeyString(Key key){
//        return Base64.getEncoder().encodeToString(key.getEncoded());
//    }
//
////    private static String randomStr(int length,char[] ch){
////        StringBuffer buffer = new StringBuffer();
////        for(int n=0;n<length;n++){
////            buffer.append(ch[(int)(Math.random()*ch.length%ch.length)]);
////        }
////        return buffer.toString();
////    }
////    /**
////     * 初始化密钥
////     *
////     * @return
////     * @throws Exception
////     */
////    public static Map<String, Object> initKey() throws Exception {
//////        BigInteger x1 = new BigInteger(
//////                "2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
//////        BigInteger x1 = new BigInteger(randomStr(41,HEX_CHAR), 16);
//////        BigInteger x2 = new BigInteger(
////////                "289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);
//////        BigInteger x2 = new BigInteger(randomStr(41,HEX_CHAR), 16);
////
//////        ECPoint g = new ECPoint(x1, x2);
//////        ECPoint g = new ECPoint(0,0);
////
////        // the order of generator
//////        BigInteger n = new BigInteger(
////////                "5846006549323611672814741753598448348329118574063", 10);
////        BigInteger n = new BigInteger(randomStr(49,DEC_CHAR), 10);
////        // the cofactor
////        int h = 2;
////        int m = 163;
////        int[] ks = { 7, 6, 3 };
////        ECFieldF2m ecField = new ECFieldF2m(m, ks);
////        // y^2+xy=x^3+x^2+1
////        BigInteger a = new BigInteger("1", 2);
////        BigInteger b = new BigInteger("1", 2);
////
////        EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);
////
////        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
////                n, h);
////        // 公钥
////        ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);
////
////        BigInteger s = new BigInteger(
////                "1234006549323611672814741753598448348329118574063", 10);
////        // 私钥
////        ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);
////
////        Map<String, Object> keyMap = new HashMap<String, Object>(2);
////
//////        keyMap.put(PUBLIC_KEY, publicKey);
//////        keyMap.put(PRIVATE_KEY, privateKey);
////
////        return keyMap;
////    }
//
//    public static KeyPair g(){
//
//        // Get domain parameters for example curve secp256r1
//        X9ECParameters ecp = getByName("sect571r1");
//        ECDomainParameters domainParams = new ECDomainParameters(ecp.getCurve(),
//                ecp.getG(), ecp.getN(), ecp.getH(),
//                ecp.getSeed());
//
//        // Generate a private key and a public key
//        AsymmetricCipherKeyPair keyPair;
//        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParams, new SecureRandom());
//        org.bouncycastle.crypto.generators.ECKeyPairGenerator generator = new org.bouncycastle.crypto.generators.ECKeyPairGenerator();
//        generator.init(keyGenParams);
//        keyPair = generator.generateKeyPair();
//
//        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();
//        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
//        byte[] privateKeyBytes = privateKey.getD().toByteArray();
//
//        // First print our generated private key and public key
//        System.out.println("Private key: " + Base64.getEncoder().encodeToString(privateKeyBytes));
//        System.out.println("Public key: " + Base64.getEncoder().encodeToString(publicKey.getQ().getEncoded(true)));
//
//        return keyPair;
//    }
//
//
//    public static KeyPair gen() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
//
////        ECGenParameterSpec ecsp = new ECGenParameterSpec("secp192k1");
//        ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256k1");
//
//        byte[] seed = new byte[8];
//        Math.random();
//        Math.random();
//        ByteUtil.writeLong(seed,new Date().getTime());
//        kpg.initialize(ecsp,new SecureRandom(seed));
//
//        KeyPair kpU = kpg.genKeyPair();
//        PrivateKey privKeyU = kpU.getPrivate();
//        PublicKey pubKeyU = kpU.getPublic();
//        System.out.println("User U: " + getKeyString(pubKeyU));
//        System.out.println("User U: " + getKeyString(privKeyU));
////
//        KeyPair kpV = kpg.genKeyPair();
//        PrivateKey privKeyV = kpV.getPrivate();
//        PublicKey pubKeyV = kpV.getPublic();
//        System.out.println("User V: " + getKeyString(pubKeyV));
//        System.out.println("User V: " + getKeyString(privKeyV));
//
//        return kpV;
////
////        KeyAgreement ecdhU = KeyAgreement.getInstance("ECDH");
////        ecdhU.init(privKeyU);
////        ecdhU.doPhase(pubKeyV,true);
////
////        KeyAgreement ecdhV = KeyAgreement.getInstance("ECDH");
////        ecdhV.init(privKeyV);
////        ecdhV.doPhase(pubKeyU,true);
////
////        System.out.println("Secret computed by U: 0x" +
////                (new BigInteger(1, ecdhU.generateSecret()).toString(16)).toUpperCase());
////        System.out.println("Secret computed by V: 0x" +
////                (new BigInteger(1, ecdhV.generateSecret()).toString(16)).toUpperCase());
//    }
//
//    public static void gen3() throws NoSuchAlgorithmException {
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//        keyPairGenerator.initialize(256);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        keyPair = keyPairGenerator.generateKeyPair();
//        ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();
//        ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate();
//
//        System.out.println("pub:"+getKeyString(ecPublicKey));
//        System.out.println("pri:"+getKeyString(ecPrivateKey));
//    }
//
//    private static String publicStr = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEcfv667X61ptg8g1Z7Zmfys2uLgijXZEBeMJG2qlmyDMEQNnuyUd0tywDORmJDY9628mtfPl7YFbyDhQtLnVsNQ==";
//    private static String privateStr = "MD4CAQAwEAYHKoZIzj0CAQYFK4EEAAoEJzAlAgEBBCCt4bzZj1ZT4fPN7xy/z6hOIuQRq5Iqe3JZVfwYsZLpag==";
//
//    public static void main(String[] args) throws Exception {
//
//        String inputStr = "3789655456:web_app";
//        byte[] data = inputStr.getBytes();
//
////        Map<String, Object> keyMap = initKey();
//
//        KeyPair keyPair = gen();
//
//        String publicKey = getKeyString(keyPair.getPublic());
//        String privateKey = getKeyString(keyPair.getPrivate());
//        System.err.println("公钥: \n" + publicKey);
//        System.err.println("私钥： \n" + privateKey);
//
//        ECPrivateKey priKey = getPrivateKey(privateStr);
//
//        byte[] encodedData = encrypt(data, (ECPublicKey) keyPair.getPublic());
//
//
//        System.out.println(encodedData.length);
//        System.out.println(new String(Base64.getEncoder().encode(encodedData)));
//
//        byte[] decodedData = decrypt(encodedData, (ECPrivateKey) keyPair.getPrivate());
//
//
//        String outputStr = new String(decodedData);
//        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
//
//
//        g();
//        g();
//        g();
//        g();
//        g();
//        g();
//        g();
//        g();
//        g();
//        g();
//        g();
//
//    }
//}
