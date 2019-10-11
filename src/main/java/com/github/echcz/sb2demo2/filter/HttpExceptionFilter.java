package com.github.echcz.sb2demo2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.echcz.sb2demo2.domain.ErrorVo;
import com.github.echcz.sb2demo2.exception.HttpException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
@WebFilter(filterName = "httpExceptionFilter", urlPatterns = {"/*"})
@Order(FilterRegistrationBean.HIGHEST_PRECEDENCE)
public class HttpExceptionFilter implements Filter {
    @NonNull
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (HttpException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(e.getStatus().value());
            httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            try (PrintWriter writer = httpResponse.getWriter()) {
                writer.write(objectMapper.writeValueAsString(new ErrorVo(e.getCode(), e.getMessage())));
            }
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
