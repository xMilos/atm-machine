package com.task.atm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StringLiterals {

    ACCOUNT_CREATED("Account created successfully"),
    ACCOUNT_DELETED("Account deleted successfully"),
    ACCOUNT_FOUND("Account found, processing further..."),
    SUCCESS_WITHDRAW("Withdrawal completed successfully"),
    BALANCE_CHECK("Checking balance"),
    BALANCE_RETRIEVED("Balance retrieved successfully");

    private String message;

}
