package io.cess.cloud.config;

import org.springframework.security.jwt.crypto.sign.Signer;

import java.security.Signature;
import java.security.interfaces.ECPrivateKey;

public class EccSigner implements Signer {

    static final String DEFAULT_ALGORITHM = "SHA1withECDSA";

    private ECPrivateKey privKey;

    public EccSigner(ECPrivateKey privateKey) {
        this.privKey = privateKey;
    }

//    public EccSigner(String key) {
//    }

    @Override
    public byte[] sign(byte[] bytes) {
        try {
            Signature ecdsa = Signature.getInstance(DEFAULT_ALGORITHM, "BC");
            ecdsa.initSign(privKey);
            ecdsa.update(bytes);
            return ecdsa.sign();
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String algorithm() {
        return DEFAULT_ALGORITHM;
    }
}
