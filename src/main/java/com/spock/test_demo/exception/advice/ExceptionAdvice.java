package com.spock.test_demo.exception.advice;

import com.spock.test_demo.common.dto.error.ResponseErrorDto;
import com.spock.test_demo.exception.ErrorCode;
import com.spock.test_demo.exception.custom.BusinessException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e,
        HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName(), e.getMessage());
        ResponseErrorDto errorResponse = ResponseErrorDto.of(ErrorCode.INVALID_INPUT_VALUE,
            e.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseErrorDto> handleConstraintViolationException(
        ConstraintViolationException e,
        HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName(), e.getMessage());
        ResponseErrorDto errorResponse = ResponseErrorDto.of(ErrorCode.INVALID_INPUT_VALUE,
            e.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ResponseErrorDto> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseErrorDto errorResponse = ResponseErrorDto.of(e, request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseErrorDto> handleBusinessException(BusinessException e,
        HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseErrorDto errorResponse = ResponseErrorDto.of(e.getErrorCode(), e.getMessage(),
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(e.getErrorCode().getCode()).body(errorResponse);
    }
}
