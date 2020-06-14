package com.precisource.handler;

import com.bleach.common.BuilderUtils;
import com.bleach.common.StringUtils;
import com.precisource.api.BaseException;
import com.precisource.api.ErrorCode;
import com.precisource.api.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;

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
    public ResponseEntity<Result> globalExceptionHandler(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        ResponseEntity responseEntity;
        if (e instanceof ConstraintViolationException) {
            responseEntity = constraintViolationExceptionHandler(req, (ConstraintViolationException) e);
        } else if (e instanceof MethodArgumentNotValidException) {
            responseEntity = methodArgumentNotValidException((MethodArgumentNotValidException) e);
        } else if (e instanceof HttpMessageNotReadableException) {
            responseEntity = httpMessageNotReadableExceptionHandler((HttpMessageNotReadableException) e);
        } else if (e instanceof BaseException) {
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
     * 参数类型验证错误，http返回状态默认400
     *
     * @param req
     * @param cve
     * @return
     */
    private ResponseEntity<Result> constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException cve) {
        logger.error("{} {} ConstraintViolationException. ", req.getMethod(), req.getRequestURI(), cve);
        String detailMessage = cve.getMessage();
        if (detailMessage.contains(StringUtils.COLON)) {
            detailMessage = detailMessage.substring(detailMessage.indexOf(StringUtils.COLON) + 1);
        }
        Result errorResult = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.CLIENT_FORMAT_ERROR)
                .with(Result::setMessage, detailMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResult);
    }

    private ResponseEntity<Result> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException cve) {
        String detailMessage = cve.getMessage();
        if (detailMessage.contains(StringUtils.COLON)) {
            detailMessage = detailMessage.substring(0, detailMessage.indexOf(StringUtils.COLON));
        }
        Result errorResult = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.CLIENT_FORMAT_ERROR)
                .with(Result::setMessage, detailMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResult);
    }

    /**
     * 使用 Validator 验证参数，在spring-boot-starter-web下自带
     *
     * @param validException
     * @return
     */
    public ResponseEntity<Result> methodArgumentNotValidException(MethodArgumentNotValidException validException) {
        List<ObjectError> errors = validException.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
        Result errorResult = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.CLIENT_FORMAT_ERROR)
                .with(Result::setMessage, errorMsg.toString())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResult);
    }

    /**
     * 特殊异常的处理
     *
     * @param baseException
     * @return
     */
    private ResponseEntity<Result> baseExceptionHandler(BaseException baseException) {
        ResponseEntity responseEntity;
        switch (baseException.getHttpStatus()) {
            case OK:
                responseEntity = ResponseEntity.status(HttpStatus.OK).build();
                break;
            case CREATED:
            case NO_CONTENT:
                responseEntity = ResponseEntity.status(baseException.getHttpStatus()).body(baseException.getMessage());
                break;
            default:
                Result errorResult = BuilderUtils.of(Result::new)
                        .with(Result::setCode, baseException.getCode())
                        .with(Result::setMessage, baseException.getMessage())
                        .build();
                responseEntity = ResponseEntity.status(baseException.getHttpStatus()).body(errorResult);
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
        Result errorResult = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.SERVER_INTERNAL_ERROR)
                .with(Result::setMessage, npe.getMessage())
                .build();
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
        Result errorResult = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.SERVER_INTERNAL_ERROR)
                .with(Result::setMessage, e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
