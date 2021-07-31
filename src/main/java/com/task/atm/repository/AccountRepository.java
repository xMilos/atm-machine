package com.task.atm.repository;

import com.task.atm.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Account repository.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumberAndPin(String accountNumber, int pin);

    void deleteByAccountNumber(String id);
}
