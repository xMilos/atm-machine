package com.task.atm.repository;

import com.task.atm.model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private final String ACC_NUMBER = "1234";
    private final int PIN = 1234;
    private final Account testAccount = new Account(null, ACC_NUMBER, PIN, 500, 50);

    @BeforeEach
    void initUseCase() {
        accountRepository.save(testAccount);
    }

    @AfterEach
    public void destroyAll() {
        accountRepository.deleteAll();
    }

    @Test
    void findByAccountNumberAndPin() {
        Account account = accountRepository.findByAccountNumberAndPin(ACC_NUMBER, PIN);
        assertEquals(account.getAccountNumber(), ACC_NUMBER);
    }

    @Test
    void deleteByAccountNumber() {
        accountRepository.deleteByAccountNumber(ACC_NUMBER);
        assertNull(accountRepository.findByAccountNumberAndPin(ACC_NUMBER, PIN));
    }
}