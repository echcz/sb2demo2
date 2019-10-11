package com.github.echcz.sb2demo2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.jsonwebtoken.io.JacksonDeserializer;
import io.jsonwebtoken.io.JacksonSerializer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class CommonConfig {
    public static final String DATE_TIME_FMT_STR = "yyyy-MM-dd HH:mm:ss";

    public static final Locale LOCALE = Locale.SIMPLIFIED_CHINESE;

    public static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern(DATE_TIME_FMT_STR, LOCALE);

    @NonNull
    private JacksonProperties jacksonProperties;


    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        String dateFormat = jacksonProperties.getDateFormat();
        if (dateFormat == null) {
            dateFormat = DATE_TIME_FMT_STR;
        }
        Locale locale = jacksonProperties.getLocale();
        if (locale == null) {
            locale = LOCALE;
        }
        return DateTimeFormatter.ofPattern(dateFormat, locale);
    }

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer(DateTimeFormatter dateTimeFormatter) {
        return new LocalDateTimeSerializer(dateTimeFormatter);
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(LocalDateTimeSerializer localDateTimeSerializer) {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeSerializer);
    }

    @Bean
    public JacksonSerializer jacksonSerializer(ObjectMapper objectMapper) {
        return new JacksonSerializer(objectMapper);
    }

    @Bean
    public JacksonDeserializer jacksonDeserializer(ObjectMapper objectMapper) {
        return new JacksonDeserializer(objectMapper);
    }


    @Bean
    public PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }
}
