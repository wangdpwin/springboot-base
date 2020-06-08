package com.precisource.handler;

import com.precisource.consts.ErrorCode;
import com.precisource.exception.BaseException;
import com.precisource.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获
 *
 * @Author: xinput
 * @Date: 2020-06-06 14:32
 */
@RestControllerAdvice
public class MyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Result> baseExceptionHandler(HttpServletRequest req, BaseException be) {
        logger.error("{} {} NullPointerException. ", req.getMethod(), req.getRequestURI(), be);
        Result result = new Result(be.getCode(), be.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Result> nullPointerExceptionHandler(HttpServletRequest req, NullPointerException npe) {
        logger.error("{} {} NullPointerException. ", req.getMethod(), req.getRequestURI(), npe);
        Result result = new Result(ErrorCode.SERVER_INTERNAL_ERROR, npe.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("{} {} Exception. ", req.getMethod(), req.getRequestURI(), e);
        Result result = new Result(ErrorCode.SERVER_INTERNAL_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}
