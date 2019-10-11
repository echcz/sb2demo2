package com.github.echcz.sb2demo2.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "jwt-auth")
public class JwtAuthProperties {
    private String headerName = "Authorization";
    private long timeout = 600000;
    private KeyType keyType = KeyType.HS256;
    private String encryptKey = null;
    private String decryptKey = null;

    public enum KeyType {
        HS256, HS384, HS512,
        RS256, RS384, RS512,
        ;
    }

}
