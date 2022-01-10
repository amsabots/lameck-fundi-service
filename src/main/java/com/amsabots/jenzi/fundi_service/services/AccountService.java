package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomResourceNotFound;
import com.amsabots.jenzi.fundi_service.repos.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepo accountRepo;

    public List<Account> getAllFundis(Pageable pageable) {
        return accountRepo.findAll(pageable).getContent();
    }

    public Account createOrUpdateAccount(Account account) {
        return accountRepo.save(account);
    }

    public void deleteAccount(long id) {
        accountRepo.deleteById(id);
    }

    public Account getAccountById(long id) {
        return accountRepo.findById(id)
                .orElseThrow(() -> new CustomResourceNotFound("Provided account details do not exist records"));
    }

    public Account getAccountByEmail(String email) {
        return accountRepo.findAccountByEmail(email).orElse(null);
    }

    public long getAvailableUsers() {
        return accountRepo.count();
    }

}
