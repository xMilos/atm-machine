package com.task.atm.service;

import com.task.atm.exceptions.BadRequestException;
import com.task.atm.exceptions.ForbiddenException;
import com.task.atm.model.Account;
import com.task.atm.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.task.atm.util.ErrorMessages.*;
import static com.task.atm.util.InitializeBankNoteState.banknoteState;
import static com.task.atm.util.StringLiterals.ACCOUNT_FOUND;

/**
 * The service class
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private Map<Integer, Integer> banknotesToGive = new HashMap<>();

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public String checkBalance(String accountNumber, int pin) {

        Account account = getAccountAndVerify(accountNumber, pin);
        return "Account balance: " + account.getBalance()
                + "Max withdrawal: " + calculateMaxWithdrawal(account.getBalance(), account.getOverdraft());
    }

    @Override
    public String withdraw(String accountNumber, int pin, int amount) {

        verifyAmountDivisibleBy5(amount);
        Account account = getAccountAndVerify(accountNumber, pin);
        int balanceAfterWithdraw =
                validateAmountAndCountBanknotes(account.getBalance() + account.getOverdraft(), amount);
        log.debug("banknotesToGive {}", banknotesToGive);
        log.debug("balanceAfterWithdraw {}", balanceAfterWithdraw);
        try {
            log.debug("Updating user balance");
            updateAccountWithNewBalance(account, balanceAfterWithdraw);
            return new StringBuilder().append("Account updated details: ")
                    .append("\n 50 euro banknotes used:").append(banknotesToGive.get(50))
                    .append("\n 20 euro banknotes used:").append(banknotesToGive.get(20))
                    .append("\n 10 euro banknotes used:").append(banknotesToGive.get(10))
                    .append("\n 5 euro banknotes used:").append(banknotesToGive.get(5))
                    .append("\n Remaining balance:").append(balanceAfterWithdraw)
                    .toString();
        } catch (Exception ex) {
            log.error("Something went wrong ", ex);
            throw new BadRequestException(ERR_002.getErrorMessage(), ERR_002.getErrorCode());
        }
    }

    @Transactional
    private Account updateAccountWithNewBalance(Account account, int balanceAfterWithdraw) {
        // openbalance 800 - draft 200 - amount 950 = 100-950 = 50 -200 = -150
        account.setBalance(balanceAfterWithdraw - account.getOverdraft());
        account = accountRepository.save(account);
        updateAtm(); // give money after transaction
        return account;
    }

    private void updateAtm() {
        banknoteState.put(50, banknoteState.get(50) - banknotesToGive.get(50));
        banknoteState.put(20, banknoteState.get(20) - banknotesToGive.get(20));
        banknoteState.put(10, banknoteState.get(10) - banknotesToGive.get(10));
        banknoteState.put(5, banknoteState.get(5) - banknotesToGive.get(5));
        banknoteState.put(0, banknoteState.get(0) - banknotesToGive.get(0));
    }

    @Override
    public Account createAccount(Account account) {
        try {
            log.debug("Create account " + account);
            return accountRepository.save(account);
        } catch (Exception ex) {
            log.error("Something went wrong ", ex);
            throw new BadRequestException(ERR_002.getErrorMessage(), ERR_002.getErrorCode());
        }
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        try {
            log.debug("Deleting account {}", accountNumber);
            accountRepository.deleteByAccountNumber(accountNumber);
        } catch (Exception ex) {
            log.error("Something went wrong ", ex);
            throw new BadRequestException(ERR_002.getErrorMessage(), ERR_002.getErrorCode());
        }
    }

    private Account getAccountAndVerify(String accountNumber, int pin) {
        log.debug("Getting account for accountNumber {}", accountNumber);
        Account account = accountRepository.findByAccountNumberAndPin(accountNumber, pin);
        if (account == null) {
            log.error("Account not found, wrong username or password");
            throw new ForbiddenException(ERR_001.getErrorMessage(), ERR_001.getErrorCode());
        }
        log.info(ACCOUNT_FOUND.getMessage());
        return account;
    }

    private void verifyAmountDivisibleBy5(int amount) {
        if (amount % 5 != 0) {
            log.error("amount cannot be paid by the ATM");
            throw new BadRequestException(ERR_004.getErrorMessage(), ERR_004.getErrorCode());
        }

    }

    private int calculateMaxWithdrawal(int balance, int overdraft) {
        log.debug("calculating withdrawal return max of atm max limit or users balance + overdraft");
        return (balance + overdraft) < banknoteState.get(0) ? balance + overdraft : banknoteState.get(0);
    }


    private int validateAmountAndCountBanknotes(int totalBalance, int amount) {
        if (totalBalance < amount) {
            log.error("total balance is lower then amount");
            throw new BadRequestException(ERR_003.getErrorMessage(), ERR_003.getErrorCode());
        }
        if (amount > banknoteState.get(0)) {
            log.error("amout is larger then atm maximum");
            throw new BadRequestException(ERR_006.getErrorMessage(), ERR_006.getErrorCode());
        }
        log.debug("calculating used bills in atm");
        atmUsedBills(amount);
        return totalBalance - amount;
    }

    private void atmUsedBills(int amount) {
        // keep track of used bills and total amount initialize
        banknotesToGive.put(0, amount);
        banknotesToGive.put(50, 0);
        banknotesToGive.put(20, 0);
        banknotesToGive.put(10, 0);
        banknotesToGive.put(5, 0);
        // start with 50 bills (as it should give min banknotes)
        if (amount >= 50) { // 845/50 = 16
            int division = amount / 50; // 16
            int reminder = amount % 50; // 45
            if (division <= banknoteState.get(50) && reminder == 0) {// ---450 - 9 bills
                banknotesToGive.put(50, division); // - 450
                return;
            } else if (division >= banknoteState.get(50)) { // 16 > 10 - all 10 bills
                amount = amount - banknoteState.get(50) * 50; // 345 reminder
                banknotesToGive.put(50, banknoteState.get(50)); // all 50 spent
            } else { // --- 445 - 8 bills 45 reminder
                amount = reminder; // amount = reminder
                banknotesToGive.put(50, division);//8
            }
        }
        if (amount >= 20) { //345
            int division = amount / 20; // 17
            int reminder = amount % 20; // 5
            if (division <= banknoteState.get(20) && reminder == 0) {// --- 100 - 5 bills
                banknotesToGive.put(20, division);// - 5
                return;
            } else if (division >= banknoteState.get(20)) { // (17 > 30 not)  --- (650) 32>30
                amount = amount - banknoteState.get(20) * 20; // 650 - 30*20 = 50
                banknotesToGive.put(20, banknoteState.get(20)); // all 30 spent
            } else { // 5
                amount = reminder; // amount = reminder 345 - 340-> 5
                banknotesToGive.put(20, division);// 17 bills spent
            }
        }
        if (amount >= 10) { //5
            int division = amount / 10; // 0
            int reminder = amount % 10; // 5
            if (division <= banknoteState.get(10) && reminder == 0) {// --- 120 - 12 bills
                banknotesToGive.put(10, division);// - 12
                return;
            } else if (division >= banknoteState.get(10)) { // (0 > 30 not)  --- (320) 32>30
                amount = amount - banknoteState.get(10) * 10; // 320 - 300*100 = 20
                banknotesToGive.put(20, banknoteState.get(10)); // all 30 spent
            } else { //5 - 0 bills
                amount = reminder; // amount = reminder 5 - 0*10-> 5
                banknotesToGive.put(10, division);// 0 bills spent
            }
        }
        if (amount >= 5) { //5
            int division = amount / 5; // 1
            int reminder = amount % 5; // 0
            if (division <= banknoteState.get(5) && reminder == 0) {// 1 < 20
                banknotesToGive.put(5, division);// - 1
                return;
            }
        }
        throw new BadRequestException(ERR_002.getErrorMessage(), ERR_002.getErrorCode()); // should not reach code
    }
}
