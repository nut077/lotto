package com.github.nut077.lotto.controller.advice;

import com.github.nut077.lotto.exception.CommonException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CommonAdvice extends ResponseEntityExceptionHandler {

    // 1
    @Getter
    @Setter
    private class ExceptionResponse {
        private String code;
        private String message;
        private LocalDateTime timestamp;
    }

    // 2
    private ResponseEntity<Object> handle(Exception ex, HttpStatus status, String code) {
        ex.printStackTrace();
        ExceptionResponse response = new ExceptionResponse();
        response.setCode("xxx-" + code);
        response.setMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-info", "https://github.com/nut077/" + code);
        return ResponseEntity.status(status).headers(headers).body(response);
    }

    // 3
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(ex, status, String.valueOf(status.value()));
    }

    // 4
    @ExceptionHandler(CommonException.class)
    protected ResponseEntity<Object> handle(CommonException ex) {
        return handle(ex, ex.getStatus(), ex.getCode());
    }

    // 5
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception ex) {
        return handle(ex, HttpStatus.INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    // 6
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handle(RuntimeException ex) {
        return handle(ex, HttpStatus.BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }
}
