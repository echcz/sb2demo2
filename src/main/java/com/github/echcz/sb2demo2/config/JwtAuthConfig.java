package com.github.echcz.sb2demo2.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.JacksonDeserializer;
import io.jsonwebtoken.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class JwtAuthConfig {
    @NonNull
    private JwtAuthProperties jwtAuthProperties;

    @Bean
    public Key encryptKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String encryptKey = jwtAuthProperties.getEncryptKey();
        String name = jwtAuthProperties.getKeyType().name();
        if (StringUtils.isEmpty(encryptKey)) {
            if (name.startsWith("RS")) {
                throw new RuntimeException("使用RSA非对称密码时必须提供私钥(jwt-auth.encrypt-key)");
            }
            return Keys.secretKeyFor(SignatureAlgorithm.valueOf(jwtAuthProperties.getKeyType().name()));
        }
        if (name.startsWith("RS")) {
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(encryptKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } else {
            return Keys.hmacShaKeyFor(Base64Utils.decodeFromString(encryptKey));
        }
    }

    @Bean
    public Key decryptKey(Key encryptKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String decryptKey = jwtAuthProperties.getDecryptKey();
        String name = jwtAuthProperties.getKeyType().name();
        if (StringUtils.isEmpty(decryptKey)) {
            if (name.startsWith("RS")) {
                throw new RuntimeException("使用RSA非对称密码时必须提供公钥(jwt-auth.decrypt-key)");
            }
            return encryptKey;
        }
        if (name.startsWith("RS")) {
            KeySpec keySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(decryptKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } else {
            return Keys.hmacShaKeyFor(Base64Utils.decodeFromString(decryptKey));
        }
    }


    @Bean
    @SuppressWarnings("unchecked")
    public Supplier<JwtBuilder> jwtBuilderSupplier(JacksonSerializer jacksonSerializer, Key encryptKey) {
        return () -> Jwts.builder()
                .serializeToJsonWith(jacksonSerializer)
                .signWith(encryptKey)
                .setExpiration(new Date(System.currentTimeMillis() + jwtAuthProperties.getTimeout()));
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Supplier<JwtParser> jwtParserSupplier(JacksonDeserializer jacksonDeserializer, Key decryptKey) {
        return () -> Jwts.parser()
                .deserializeJsonWith(jacksonDeserializer)
                .setSigningKey(decryptKey);
    }
}
