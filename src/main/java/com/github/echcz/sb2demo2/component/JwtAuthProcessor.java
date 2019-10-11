package com.github.echcz.sb2demo2.component;

import com.github.echcz.sb2demo2.config.JwtAuthProperties;
import com.github.echcz.sb2demo2.constant.ErrorStatus;
import com.github.echcz.sb2demo2.exception.HttpClientException;
import com.github.echcz.sb2demo2.model.SingleEnumeration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class JwtAuthProcessor implements AuthProcessor {

    @NonNull
    private JwtAuthProperties jwtAuthProperties;
    @NonNull
    private Supplier<JwtParser> jwtParserSupplier;

    @Override
    public HttpServletRequest process(HttpServletRequest request) {
        Claims claims;
        try {
            claims = jwtParserSupplier.get()
                    .parseClaimsJws(request.getHeader(jwtAuthProperties.getHeaderName()))
                    .getBody();
        } catch (Exception e) {
            throw new HttpClientException(HttpStatus.UNAUTHORIZED,
                    ErrorStatus.UNAUTHORIZED.getCode(), ErrorStatus.UNAUTHORIZED.getMsg());
        }
        String userId = claims.getAudience() == null ? "0" : claims.getAudience();
        String roles = claims.getSubject() == null ? "" : claims.getSubject();
        return new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                switch (name) {
                    case AuthProcessor.USER_ID_HEADER_NAME:
                        return userId;
                    case ROLES_HEADER_NAME:
                        return roles;
                    default:
                        return super.getHeader(name);
                }
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                switch (name) {
                    case AuthProcessor.USER_ID_HEADER_NAME:
                        return new SingleEnumeration<>(userId);
                    case ROLES_HEADER_NAME:
                        return new SingleEnumeration<>(roles);
                    default:
                        return super.getHeaders(name);
                }
            }
        };
    }
}
