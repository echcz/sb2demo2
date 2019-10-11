package com.github.echcz.sb2demo2.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {
    private Integer userId = 1;
    private String username = "admin";
    private String password = "admin";
    private List<String> roles = new ArrayList<>();
}
