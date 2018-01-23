//package io.cess.util;
//
//import de.flexiprovider.common.ies.IESParameterSpec;
//import de.flexiprovider.core.FlexiCoreProvider;
//import de.flexiprovider.ec.FlexiECProvider;
//import de.flexiprovider.ec.parameters.CurveParams;
//import de.flexiprovider.ec.parameters.CurveRegistry;
//
//import javax.crypto.Cipher;
//import java.security.*;
//import java.util.Base64;
//
//public class Ecc2 {
//
//    public static void main(String[] args)throws Exception{
//
//        //Security.setProperty("crypto.policy", "unlimited");
//
//        Security.addProvider(new FlexiCoreProvider());
//        Security.addProvider(new FlexiECProvider());
//
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");
//
//        CurveParams ecParams = new CurveRegistry.BrainpoolP512r1();
//
//
//        kpg.initialize(ecParams, new SecureRandom());
//        KeyPair keyPair = kpg.generateKeyPair();
//        PublicKey pubKey = keyPair.getPublic();
//        PrivateKey privKey = keyPair.getPrivate();
//
//        Cipher cipher = Cipher.getInstance("ECIES", "FlexiEC");
//
//        IESParameterSpec iesParams = new IESParameterSpec("AES128_CBC",
//                "HmacSHA1", null, null);
//        cipher.init(Cipher.ENCRYPT_MODE, pubKey,iesParams);
//
//        byte[] bs = cipher.doFinal("ok".getBytes());
//
//        System.out.println(new String(Base64.getEncoder().encode(bs)));
//
//    }
//}
