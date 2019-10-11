package com.github.echcz.sb2demo2.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    @NonNull
    private CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (corsProperties.isEnableCors()) {
            for (CorsProperties.CorsMapper corsMapper : corsProperties.getCorsMappers()) {
                registry.addMapping(corsMapper.getMapping())
                        .allowedOrigins(corsMapper.getOrigins())
                        .exposedHeaders(corsMapper.getExposedHeaders())
                        .allowCredentials(corsMapper.isAllowCredentials())
                        .allowedMethods(corsMapper.getMethods())
                        .allowedHeaders(corsMapper.getHeaders())
                        .maxAge(corsMapper.getMaxAge());
            }
        }
    }
}
