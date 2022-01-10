package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */


@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    public Optional<Account> findAccountByAccountId(String id);

    public Optional<Account> findAccountByEmail(String emailOrPhoneNumber);

}
