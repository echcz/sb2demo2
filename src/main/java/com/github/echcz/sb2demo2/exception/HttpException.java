package com.github.echcz.sb2demo2.exception;

import com.github.echcz.sb2demo2.constant.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    @Getter
    private final HttpStatus status;
    @Getter
    private final int code;

    public HttpException(HttpStatus status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpException(HttpStatus status, int code) {
        this(status, code, ErrorStatus.UNKNOWN.getMsg());
    }

    public HttpException(HttpStatus status) {
        this(status, ErrorStatus.UNKNOWN.getCode());
    }

}
