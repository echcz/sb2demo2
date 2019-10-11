package com.github.echcz.sb2demo2.config;

import com.github.echcz.sb2demo2.constant.ErrorStatus;
import com.github.echcz.sb2demo2.domain.ErrorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class GlobalErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<ErrorVo> error(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        String method = request.getMethod();
        Object uri = request.getAttribute("javax.servlet.error.request_uri");
        Object msg = request.getAttribute("javax.servlet.error.message");
        log.warn("method={};uri={};status={};msg={}", method, uri, status, msg);
        ErrorVo body;
        switch (status) {
            case 404:
                body = ErrorVo.fromErrorStatus(ErrorStatus.NOT_FOUND);
                break;
            case 503:
                body = ErrorVo.fromErrorStatus(ErrorStatus.SERVER_UNAVAILABLE);
                break;
            default:
                if (status < 500) {
                    body = ErrorVo.fromErrorStatus(ErrorStatus.CLIENT_ERROR);
                } else {
                    body = ErrorVo.fromErrorStatus(ErrorStatus.SERVER_ERROR);
                }
        }
        return new ResponseEntity<>(body, HttpStatus.valueOf(status));

    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
