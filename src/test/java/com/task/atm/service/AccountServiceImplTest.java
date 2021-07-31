package com.task.atm.service;

import com.task.atm.exceptions.BadRequestException;
import com.task.atm.model.Account;
import com.task.atm.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService = new AccountServiceImpl();

    @Mock
    private AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkBalanceTest() {
        Mockito.when(accountRepository.findByAccountNumberAndPin("1", 1))
                .thenReturn(new Account(null, "1", 1, 300, 300));
        String str = accountService.checkBalance("1", 1);
        Assert.assertEquals(str, "Account balance: 300Max withdrawal: 600");
    }

    @Test
    public void withdrawTest() {
        Mockito.when(accountRepository.findByAccountNumberAndPin("1", 1))
                .thenReturn(new Account(null, "1", 1, 300, 300));
        String str = accountService.withdraw("1", 1, 550);
        Assert.assertEquals(str, "Account updated details: \n" +
                " 50 euro banknotes used:10\n" +
                " 20 euro banknotes used:2\n" +
                " 10 euro banknotes used:1\n" +
                " 5 euro banknotes used:0\n" +
                " Remaining balance:50");
    }

    @Test(expected = BadRequestException.class)
    public void withdrawNoFundsTest() {
        Mockito.when(accountRepository.findByAccountNumberAndPin("1", 1))
                .thenReturn(new Account(null, "1", 1, 300, 300));
        accountService.withdraw("1", 1, 650);
        Assert.fail();
    }

    @Test(expected = BadRequestException.class)
    public void withdrawAmountNotDivisibleWithFive() {
        Mockito.when(accountRepository.findByAccountNumberAndPin("1", 1))
                .thenReturn(new Account(null, "1", 1, 300, 300));
        accountService.withdraw("1", 1, 651);
        Assert.fail();
    }

}