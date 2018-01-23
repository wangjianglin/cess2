package io.cess.cloud.config;

import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.security.Signature;
import java.security.interfaces.ECPublicKey;

public class EccVerifier implements SignatureVerifier {
    private static final String DEFAULT_ALGORITHM = "SHA1withECDSA";
    private static final String PROVIDER = "BC";
    private ECPublicKey publicKey;
    public EccVerifier(ECPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public void verify(byte[] content, byte[] sig) {
        try {
            Signature signature = Signature.getInstance(DEFAULT_ALGORITHM, PROVIDER);
            signature.initVerify(publicKey);
            signature.update(content);
            if(!signature.verify(sig)){
                throw new InvalidSignatureException("ECC Signature did not match content");
            }
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String algorithm() {
        return DEFAULT_ALGORITHM;
    }
}
