package com.demo.spring.exception;

import ch.qos.logback.core.status.Status;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource not found
    static Timestamp ts;
    static Status sts;
    static Error err;
    static Message msg;
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("ts", LocalDateTime.now());
        error.put("sts", HttpStatus.NOT_FOUND.value());
        error.put("err", "Not Found");
        error.put("msg", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 400 - Bad request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("ts", LocalDateTime.now());
        error.put("sts", HttpStatus.BAD_REQUEST.value());
        error.put("err", "Bad Request");
        error.put("msg", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //  500 - Any other exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("ts", LocalDateTime.now());
        error.put("sts", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("err", "Internal Server Error");
        error.put("msg", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}