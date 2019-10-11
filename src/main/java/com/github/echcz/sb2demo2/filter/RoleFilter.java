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
import java.util.List;

@RequiredArgsConstructor
@Component
@WebFilter(filterName = "roleFilter", urlPatterns = {"/*"})
@Order(2)
public class RoleFilter implements Filter {
    @NonNull
    private AuthProperties authProperties;
    @NonNull
    private PathMatcher pathMatcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 优化
        if ((httpRequest.getHeader(AuthProcessor.USER_ID_HEADER_NAME) == null) ||
                authProperties.getAuthPath().getRoleList().isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        String uri = httpRequest.getRequestURI().toLowerCase();
        String method = "," + httpRequest.getMethod().toLowerCase() + ",";
        String roleStr = httpRequest.getHeader(AuthProcessor.ROLES_HEADER_NAME);
        String[] roles = roleStr.split("\\s*,\\s*");
        for (int i = 0; i < roles.length; i++) {
            roles[i] = "," + roles[i].toLowerCase() + ",";
        }
        if (matchRole(authProperties.getAuthPath().getRoleList(), roles, uri, method)) {
            chain.doFilter(request, response);
        } else {
            throw new HttpClientException(HttpStatus.FORBIDDEN, ErrorStatus.FORBIDDEN.getCode(), ErrorStatus.FORBIDDEN.getMsg());
        }
    }

    private boolean matchRole(List<String> patterns, String[] roles, String uri, String method) {
        for (String pattern : patterns) {
            String[] split = pattern.split("\\s+");
            String methods = "," + split[0] + ",";
            String methodWildcard = "," + AuthProcessor.METHOD_WILDCARD + ",";
            if (methods.contains(methodWildcard) || methods.contains(method)) {
                if (pathMatcher.matchStart(split[1], uri)) {
                    String roleRequirement = "," + split[2] + ",";
                    for (String role : roles) {
                        if (roleRequirement.contains(role)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
