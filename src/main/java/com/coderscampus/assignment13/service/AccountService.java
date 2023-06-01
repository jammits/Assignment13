package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public Account findById(Long accountId) {
        Optional<Account> account = accountRepo.findById(accountId);
        return account.get();
    }
    public Account save(Account acc) {
        return accountRepo.save(acc);
    }
}
