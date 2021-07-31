package com.task.atm.controller;

import com.google.gson.Gson;
import com.task.atm.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.task.atm.util.ErrorMessages.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    void shouldCheckBalance() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/balance")
                .param("accountNumber", "123456789")
                .param("pin", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Account balance")));

    }

    @Test
    void shouldNotCheckBalanceWrongPin() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/balance")
                .param("accountNumber", "123456789")
                .param("pin", "123"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ERR_001.getErrorCode() + ":" + ERR_001.getErrorMessage()));

    }

    @Test
    void shouldNotCheckBalanceWrongAccNumber() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/balance")
                .param("accountNumber", "123456789")
                .param("pin", "123"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ERR_001.getErrorCode() + ":" + ERR_001.getErrorMessage()));
    }

    @Test
    void shouldNotCheckBalancePinIsNotInteger() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/balance")
                .param("accountNumber", "123456789")
                .param("pin", "12a3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldWithdraw() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/withdraw")
                .param("accountNumber", "123456789")
                .param("pin", "1234")
                .param("withDrawAmount", "1000"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Remaining balance:")));
    }

    @Test
    void shouldNotWithdrawWrongPin() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/withdraw")
                .param("accountNumber", "123456789")
                .param("pin", "12345")
                .param("withDrawAmount", "5550"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ERR_001.getErrorCode() + ":" + ERR_001.getErrorMessage()));
    }

    @Test
    void shouldNotWithdrawInsufficientFunds() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/withdraw")
                .param("accountNumber", "123456789")
                .param("pin", "1234")
                .param("withDrawAmount", "5550"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERR_003.getErrorCode() + ":" + ERR_003.getErrorMessage()));
    }

    @Test
    void shouldNotWithdrawWrongAmountNotDivisable() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/withdraw")
                .param("accountNumber", "123456789")
                .param("pin", "1234")
                .param("withDrawAmount", "424"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERR_004.getErrorCode() + ":" + ERR_004.getErrorMessage()));
    }

    @Test
    void shouldNotWithdrawAtmNoCash() throws Exception {

        this.mockMvc.perform(post("/api/v1/account/withdraw")
                .param("accountNumber", "987654321")
                .param("pin", "4321")
                .param("withDrawAmount", "550"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERR_006.getErrorCode() + ":" + ERR_006.getErrorMessage()));
    }

    @Test
    void shouldCreateAccount() throws Exception {
        Account account = new Account(null, "123", 123, 1200, 200);

        this.mockMvc.perform(post("/api/v1/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(account)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        this.mockMvc.perform(delete("/api/v1/account/delete/{accountNumber}", "1234"))
                .andExpect(status().isOk());
    }
}