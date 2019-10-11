package com.github.echcz.sb2demo2.config;

import com.github.echcz.sb2demo2.constant.ErrorStatus;
import com.github.echcz.sb2demo2.domain.ErrorVo;
import com.github.echcz.sb2demo2.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorVo handleException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<ObjectError> allErrors = result.getAllErrors();
        List<String> msgList = allErrors.stream().map(ObjectError::getDefaultMessage)
                .filter(item -> !StringUtils.isEmpty(item)).collect(Collectors.toList());
        if (msgList.isEmpty()) {
            return ErrorVo.fromErrorStatus(ErrorStatus.ARGUMENT_NOT_VALID);
        }
        return new ErrorVo(ErrorStatus.ARGUMENT_NOT_VALID.getCode(), String.join("; ", msgList));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorVo> handleHttpException(HttpException e) {
        return new ResponseEntity<>(new ErrorVo(e.getCode(), e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorVo handleServletRequestBindingException(ServletException e) {
        return new ErrorVo(ErrorStatus.BAD_REQUEST.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorVo handleException(Exception e) {
        return new ErrorVo(ErrorStatus.UNKNOWN.getCode(), e.getMessage());
    }

}
