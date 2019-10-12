package com.github.echcz.sb2demo2;

import com.github.echcz.sb2demo2.config.CorsProperties;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTests {
    @Autowired
    private Key encryptKey;
    @Autowired
    private Key decryptKey;

    @Test
    public void testHSKey() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        printKey("key", key);

        byte[] keyBytes = key.getEncoded();
        SecretKey newKey = Keys.hmacShaKeyFor(keyBytes);
        printKey("newKey", newKey);

        Assert.assertEquals(key, newKey);
    }

    @Test
    public void testRSAKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey priKey = keyPair.getPrivate();
        printKey("pubKey", pubKey);
        printKey("priKey", priKey);

        byte[] pubBytes = pubKey.getEncoded();
        byte[] priBytes = priKey.getEncoded();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubBytes);
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priBytes);
        KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
        PublicKey newPubKey = rsaFactory.generatePublic(pubKeySpec);
        PrivateKey newPriKey = rsaFactory.generatePrivate(priKeySpec);
        printKey("newPubKey", newPubKey);
        printKey("newPriKey", newPriKey);

        Assert.assertEquals(pubKey, newPubKey);
        Assert.assertEquals(priKey, newPriKey);
    }

    @Test
    public void testJwtAuthKey() {
        printKey("encrypt", encryptKey);
        printKey("decrypt", decryptKey);
    }

    private void printKey(String name, Key key) {
        System.out.println(name + ": " + key.getAlgorithm() + "=>" + key.getFormat() + "=>" + Base64Utils.encodeToString(key.getEncoded()));

    }

}
