package com.task.atm.service;

import com.task.atm.model.Account;

public interface AccountService {

    Account createAccount(Account account);

    String checkBalance(String accountNumber, int pin);

    String withdraw(String accountNumber, int pin, int withDrawnAmount);

    void deleteAccount(String id);
}
