package com.precisource.handler;

import com.precisource.api.ErrorResult;
import com.precisource.consts.ErrorCode;
import com.precisource.exception.BaseException;
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
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ErrorResult> baseExceptionHandler(BaseException be) {
        ResponseEntity responseEntity;
        switch (be.getHttpStatus()) {
            case OK:
                responseEntity = ResponseEntity.status(HttpStatus.OK).build();
                break;
            case CREATED:
            case NO_CONTENT:
                responseEntity = ResponseEntity.status(be.getHttpStatus()).body(be.getMessage());
                break;
            default:
                ErrorResult errorResult = new ErrorResult(be.getCode(), be.getMessage());
                responseEntity = ResponseEntity.status(be.getHttpStatus()).body(errorResult);
                break;
        }

        return responseEntity;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResult> nullPointerExceptionHandler(HttpServletRequest req, NullPointerException npe) {
        logger.error("{} {} NullPointerException. ", req.getMethod(), req.getRequestURI(), npe);
        ErrorResult errorResult = new ErrorResult(ErrorCode.SERVER_INTERNAL_ERROR, npe.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResult> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("{} {} Exception. ", req.getMethod(), req.getRequestURI(), e);
        ErrorResult errorResult = new ErrorResult(ErrorCode.SERVER_INTERNAL_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
