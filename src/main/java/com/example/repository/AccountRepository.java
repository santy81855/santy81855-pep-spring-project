package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    /*
     * @param username of the account.
     * @return account with the username provided as the parameter.
     */
    @Query("FROM Account WHERE username = :usernameVar")
    Account getByUsername(@Param("usernameVar") String username);

    /*
     * @param username & password of the account
     * @return account with the username and password provided as the parameters
     */
    @Query("FROM Account WHERE username = :usernameVar AND password = :passwordVar")
    Account getByUsernamePassword(@Param("usernameVar") String username, @Param("passwordVar") String password);
}
