package com.spock.test_demo.exception.custom;

import com.spock.test_demo.exception.ErrorCode;

public class ResourceNotFoundException extends BusinessException{

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
