package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.entities.Fundi_Account_Overall_Perfomance;
import com.amsabots.jenzi.fundi_service.entities.RedisLocationAlgo;
import com.amsabots.jenzi.fundi_service.enumUtils.AccountProviders;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomBadRequest;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomForbiddenResource;
import com.amsabots.jenzi.fundi_service.repos.AccountRepo;
import com.amsabots.jenzi.fundi_service.services.AccountService;
import com.amsabots.jenzi.fundi_service.services.LocationGenerator;
import com.amsabots.jenzi.fundi_service.services.OverallPerfomanceService;
import com.amsabots.jenzi.fundi_service.utils.NearbyAccounts;
import com.amsabots.jenzi.fundi_service.utils.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
@RestController
@RequestMapping("/accounts")
@Slf4j
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;
    private PasswordEncoder encoder;
    private OverallPerfomanceService perfomanceService;
    private AccountRepo repo;
    private LocationGenerator locationGenerator;

    /**
     * This request controller is only accessible by any user with system admin roles.
     * Should be configured to follow spring boot security configurations protocols and semantics
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<Account>> getAllFundis(@RequestParam Optional<Integer> page,
                                                                @RequestParam Optional<Integer> pageSize) {
        int page_size = pageSize.orElse(10);
        int current_page = page.orElse(0);
        // =====
        Pageable pageable = PageRequest.of(current_page, page_size);
        return ResponseEntity.ok(new ResponseObject<>(accountService.getAllFundis(pageable), page_size, current_page));
    }

    //admin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/count")
    public ResponseEntity<String> getRecordsCount() {
        long count = accountService.getAvailableUsers();
        return ResponseEntity.ok().body(String.format("{\"message\":%s}", count));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if (account.getAccountId() != null && account.getAccountProviders() == null)
            throw new CustomBadRequest(String.format("You are using an external account providers \"accountProviders\" with email or phone, you must provide a provider type explicitly\n" +
                    "Options include %s and %s", AccountProviders.GOOGLE, AccountProviders.PHONENUMBER));
        if (account.getAccountId() == null && account.getPassword() == null)
            throw new CustomBadRequest("Creating account like this requires email and password provided as part of the request body");

        Account a = accountService.getAccountByEmail(account.getEmail());
        if (null != a)
            throw new CustomForbiddenResource("The email or phone number you have provided already exists in our database");

        if (account.getPassword() != null) account.setPassword(encoder.encode(account.getPassword()));
        Account new_account = accountService.createOrUpdateAccount(account);

//        save account performance defaults parameters
        Fundi_Account_Overall_Perfomance performance = new Fundi_Account_Overall_Perfomance();
        performance.setAccount(new_account);

        perfomanceService.createOrUpdate(performance);
        //new_account.setOverallPerfomance(performance);

        return ResponseEntity.status(HttpStatus.OK).body(new_account);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateFundiAccount(@RequestBody Account account, @PathVariable long id) {
        Account a = accountService.getAccountById(id);
        account.setPassword(a.getPassword());
        account.setId(id);
        repo.save(account);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"The fundi details have been updated successfully\"}");
    }

    @PutMapping(path = "/password/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@RequestBody Account account, @PathVariable long id) {
        if (null == account.getPassword()) throw new CustomBadRequest("Password field is required");
        Account a = accountService.getAccountById(id);
        a.setPassword(encoder.encode(account.getPassword()));
        accountService.createOrUpdateAccount(a);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Password has been updated successfully\"}");
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"The Fundi account has been deleted successfully from the system\"}");
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getFundiById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/login")
    public ResponseEntity<String> login(@RequestBody Account account) {
        Account a = accountService.getAccountByEmail(account.getEmail());
        if (null == a)
            throw new CustomForbiddenResource("The email or phone number used does not exists in our records");
        if (!encoder.matches(account.getPassword(), a.getPassword()))
            throw new CustomForbiddenResource("The password or email used is invalid");
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Welcome to the our service. It is always good to see you\"}");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/find-nearby")
    public ResponseEntity<List<NearbyAccounts>> getNearbyUsers(
            @RequestParam(required = true) double latitude,
            @RequestParam(required = true) double longitude,
            @RequestParam Optional<Double> scanRadius) {
        List<NearbyAccounts> nearbyAccounts = new ArrayList<>();
        double r = scanRadius.orElse(15.0);
        List<RedisLocationAlgo> locations = locationGenerator.queryByProximityRadius(
                new Point(longitude, latitude), r
        );
        locations.forEach(e -> {
            Account a = repo.findAccountByAccountId(e.getAccountId()).orElse(null);
            NearbyAccounts accounts = new NearbyAccounts();
            if (a != null && !a.isEngaged()) {
                accounts.setAccount(a);
                accounts.setDistance(e.getDistance());
                nearbyAccounts.add(accounts);
            }
        });
        return ResponseEntity.ok(nearbyAccounts);
    }


}
