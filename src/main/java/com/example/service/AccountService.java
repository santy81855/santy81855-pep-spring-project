package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    // constructor injection to inject the AccountRepository class into the AccountService class
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(Account account) {
        return accountRepository.findById(account.getAccountId()).orElse(null);
    }

    public Account login(Account account) {
        return accountRepository.getByUsernamePassword(account.getUsername(), account.getPassword());
    }

    public Account createAccount(Account account) {
        // Valid if username is not blank, the password is at least 4 characters long.
        if (account.getUsername() == null || account.getUsername().length() == 0 || account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        // Check if an acount with that username already exists
        Account accountWithUsername = accountRepository.getByUsername(account.getUsername());
        if (accountWithUsername != null) {
            accountWithUsername.setAccountId(-1);
            return accountWithUsername;
        }
        // create the account
        return accountRepository.save(account);
    }
}
