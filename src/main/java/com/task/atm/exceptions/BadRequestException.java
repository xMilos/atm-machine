package com.task.atm.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private String errorMessage;
    private String errorCode;

    public BadRequestException(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
