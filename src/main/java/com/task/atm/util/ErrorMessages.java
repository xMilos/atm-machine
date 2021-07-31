package com.task.atm.util;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessages {

    ERR_001("Wrong account number or pin.",
            "com.task.atm.E_FORBIDDEN"),
    ERR_002("Something went wrong, bad request",
            "com.task.atm.E_BAD_REQUEST_GENERAL"),
    ERR_003("Insufficient funds",
            "com.task.atm.E_BAD_REQUEST_FUNDS"),
    ERR_004("Amount must be multiplied by 5",
            "com.task.atm.E_BAD_REQUEST_MODULO"),
    ERR_006("ATM cannot serve desired amount",
            "com.task.atm.E_BAD_REQUEST_NO_BILLS");


    private String errorMessage;
    private String errorCode;
}
