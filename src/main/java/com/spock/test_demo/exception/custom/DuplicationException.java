package com.spock.test_demo.exception.custom;


import com.spock.test_demo.exception.ErrorCode;

public class DuplicationException extends BusinessException {


    public DuplicationException(String message) {
        super(ErrorCode.INVALID_INPUT_VALUE, message);
    }
}
