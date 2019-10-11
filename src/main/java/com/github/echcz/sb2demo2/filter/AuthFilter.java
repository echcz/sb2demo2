package com.github.echcz.sb2demo2.filter;

import com.github.echcz.sb2demo2.component.AuthProcessor;
import com.github.echcz.sb2demo2.config.AuthProperties;
import com.github.echcz.sb2demo2.constant.ErrorStatus;
import com.github.echcz.sb2demo2.exception.HttpClientException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@WebFilter(filterName = "authFilter", urlPatterns = {"/*"})
@Order(1)
public class AuthFilter implements Filter {
    @NonNull
    private AuthProperties authProperties;
    @NonNull
    private PathMatcher pathMatcher;
    @NonNull
    private AuthProcessor authProcessor;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        checkHeader(httpRequest);
        String uri = httpRequest.getRequestURI().toLowerCase();
        String method = "," + httpRequest.getMethod().toLowerCase() + ",";
        AuthProperties.AuthPath authPath = authProperties.getAuthPath();
        for (String pattern : authPath.getWhiteList()) {
            if (matchUri(pattern, uri, method)) {
                chain.doFilter(request, response);
                return;
            }
        }
        for (String pattern : authPath.getBlackList()) {
            if (matchUri(pattern, uri, method)) {
                throw new HttpClientException(HttpStatus.FORBIDDEN,
                        ErrorStatus.FORBIDDEN.getCode(), ErrorStatus.FORBIDDEN.getMsg());
            }
        }
        for (String pattern : authPath.getCheckList()) {
            if (matchUri(pattern, uri, method)) {
                chain.doFilter(authProcessor.process(httpRequest), response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void checkHeader(HttpServletRequest request) throws HttpClientException {
        if ((request.getHeader(AuthProcessor.USER_ID_HEADER_NAME) != null)
                || (request.getHeader(AuthProcessor.ROLES_HEADER_NAME) != null)) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, ErrorStatus.BAD_REQUEST.getCode(), ErrorStatus.BAD_REQUEST.getMsg());
        }
    }

    private boolean matchUri(String pattern, String uri, String method) {
        String[] split = pattern.split("\\s+");
        String methods = "," + split[0] + ",";
        String methodWildcard = "," + AuthProcessor.METHOD_WILDCARD + ",";
        if (methods.contains(methodWildcard) || methods.contains(method)) {
            return pathMatcher.matchStart(split[1], uri);
        }
        return false;
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
