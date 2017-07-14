package com.jxtii.solr.controller;

import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by guolf on 17/7/3.
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "参数格式有误")
    @ExceptionHandler({JsonSyntaxException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity handleIOException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("参数错误");
    }
}
