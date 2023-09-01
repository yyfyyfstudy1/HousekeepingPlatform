package com.usyd.capstone.common.util;

public class CustomExceptionUtil extends RuntimeException{

    private String errorCode;

    public CustomExceptionUtil(String message) {
        super(message);
    }

    public CustomExceptionUtil(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
