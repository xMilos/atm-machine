package com.task.atm.controller;

import com.task.atm.exceptions.BadRequestException;
import com.task.atm.exceptions.ForbiddenException;
import com.task.atm.model.Account;
import com.task.atm.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.task.atm.util.StringLiterals.*;

/**
 * The type Account controller.
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
@Api(tags = "Account endpoints")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Check balance response entity.
     *
     * @param accountNumber the account number
     * @param pin           the pin
     * @return the response entity
     */
    @PostMapping("/balance")
    @ApiOperation(value = "Check balance")
    public ResponseEntity<String> checkBalance(@RequestParam String accountNumber, @RequestParam int pin) {
        try {
            String response = accountService.checkBalance(accountNumber, pin);
            log.info(BALANCE_RETRIEVED.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ForbiddenException ex) {
            return new ResponseEntity<>(ex.getErrorCode() + ":" + ex.getErrorMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Withdraw response entity.
     *
     * @param accountNumber   the account number
     * @param pin             the pin
     * @param withDrawAmount the with drawn amount
     * @return the response entity
     */
    @PostMapping("/withdraw")
    @ApiOperation(value = "Withdraw funds")
    public ResponseEntity<String> withdraw(@RequestParam String accountNumber, @RequestParam int pin, @RequestParam int withDrawAmount) {
        try {
            String response = accountService.withdraw(accountNumber, pin, withDrawAmount);
            log.info(SUCCESS_WITHDRAW.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ForbiddenException ex) {
            return new ResponseEntity<>(ex.getErrorCode() + ":" + ex.getErrorMessage(), HttpStatus.FORBIDDEN);
        } catch (BadRequestException ex){
            return new ResponseEntity<>(ex.getErrorCode() + ":" + ex.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create account.
     *
     * @param account the account body
     * @return the response entity
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create Account")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        try {
            accountService.createAccount(account);
            log.info(ACCOUNT_CREATED.getMessage());
            return new ResponseEntity<>(ACCOUNT_CREATED.getMessage(), HttpStatus.CREATED);
        } catch (BadRequestException ex){
            return new ResponseEntity<>(ex.getErrorCode() + ":" + ex.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete account.
     *
     * @param accountNumber accountNumber
     * @return the response entity
     */
    @DeleteMapping("/delete/{accountNumber}")
    @ApiOperation(value = "Delete Account")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        try {
            accountService.deleteAccount(accountNumber);
            log.info("Account deleted");
            return ResponseEntity.ok("Account "+accountNumber+" deleted");
        } catch (BadRequestException ex){
            return new ResponseEntity<>(ex.getErrorCode() + ":" + ex.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

