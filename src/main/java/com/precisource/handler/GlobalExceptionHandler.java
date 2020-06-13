package com.precisource.handler;

import com.precisource.api.BaseException;
import com.precisource.api.ErrorCode;
import com.precisource.api.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常捕获
 *
 * @Author: xinput
 * @Date: 2020-06-06 14:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ThreadLocal<HttpServletResponse> threadLocal;

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> globalExceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("{} {} Exception. ", req.getMethod(), req.getRequestURI(), e);

        ResponseEntity responseEntity;
        if (e instanceof BaseException) {
            responseEntity = baseExceptionHandler((BaseException) e);
        } else if (e instanceof NullPointerException) {
            responseEntity = nullPointerExceptionHandler(req, (NullPointerException) e);
        } else {
            responseEntity = exceptionHandler(req, e);
        }

        threadLocal.remove();
        return responseEntity;
    }

    /**
     * 封装的异常处理
     *
     * @param be
     * @return
     */
    private ResponseEntity<Result> baseExceptionHandler(BaseException be) {
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
                Result errorResult = new Result(be.getCode(), be.getMessage());
                responseEntity = ResponseEntity.status(be.getHttpStatus()).body(errorResult);
                break;
        }

        return responseEntity;
    }

    /**
     * 空指针异常
     *
     * @param req
     * @param npe
     * @return
     */
    private ResponseEntity<Result> nullPointerExceptionHandler(HttpServletRequest req, NullPointerException npe) {
        logger.error("{} {} NullPointerException. ", req.getMethod(), req.getRequestURI(), npe);
        Result errorResult = new Result(ErrorCode.SERVER_INTERNAL_ERROR, npe.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }

    /**
     * 其他异常
     *
     * @param req
     * @param e
     * @return
     */
    private ResponseEntity<Result> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("{} {} Exception. ", req.getMethod(), req.getRequestURI(), e);
        Result errorResult = new Result(ErrorCode.SERVER_INTERNAL_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
