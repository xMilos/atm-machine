package com.task.atm.exceptions;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private String errorMessage;
    private String errorCode;

    public ForbiddenException(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
