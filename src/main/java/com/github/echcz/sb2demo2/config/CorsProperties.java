package com.github.echcz.sb2demo2.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    /**
     * 是否启用 CORS 跨域
     */
    private boolean enableCors = false;
    private List<CorsMapper> corsMappers = List.of(new CorsMapper());

    @Data
    @NoArgsConstructor
    public static class CorsMapper {
        /**
         * 请求地址
         */
        private String mapping = "/**";
        /**
         * 允许的源列表
         */
        private String[] origins = {"*"};
        /**
         * 允许的方法列表
         */
        private String[] methods = {"*"};
        /**
         * 额外允许的请求头列表
         */
        private String[] headers = {"*"};
        /**
         * 额外公开的响应头列表
         */
        private String[] exposedHeaders = {};
        /**
         * 是否允许发送cookies
         */
        private boolean allowCredentials = false;
        /**
         * 预检请求的有效期，以秒为单位
         */
        private long maxAge = 3600;
    }

}
