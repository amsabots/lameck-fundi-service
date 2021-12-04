package com.amsabots.jenzi.fundi_service.contollers;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.enumUtils.AccountProviders;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomBadRequest;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomForbiddenResource;
import com.amsabots.jenzi.fundi_service.services.AccountService;
import com.amsabots.jenzi.fundi_service.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder encoder;

    /**
     * This request controller is only accessible by any user with system admin roles.
     * Should be configured to follow spring boot security configurations protocols and semantics
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<Account>> getAllFundis(@RequestParam Optional<Integer> page,
                                                                @RequestParam Optional<Integer> pageSize) {
        int page_size = pageSize.orElse(10);
        int current_page = page.orElse(0);
        Pageable pageable = PageRequest.of(current_page, page_size);
        return ResponseEntity.ok(new ResponseObject<>(accountService.getAllFundis(pageable), page_size, current_page));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if (account.getAccountId() != null && account.getAccountProviders() == null)
            throw new CustomBadRequest(String.format("You are using an external account provider with email or phone, you must provider a provider type explicitly\n" +
                    "Options include %s and %s", AccountProviders.GOOGLE, AccountProviders.PHONENUMBER));
        if (account.getAccountId() == null && account.getPassword() == null)
            throw new CustomBadRequest("Creating account like this requires email and password provided as part of the request body");

        Account a = accountService.getAccountByEmail(account.getEmail());
        if (null != a)
            throw new CustomForbiddenResource("The email or phone number you have provided already exists in our database");

        if (account.getPassword() != null) account.setPassword(encoder.encode(account.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(accountService.createOrUpdateAccount(account));
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateFundiAccount(@RequestBody Account account, @PathVariable String id) {
        account.setId(Long.valueOf(id));
        Account a = accountService.getAccountById(id);
        account.setPassword(a.getPassword());

        accountService.createOrUpdateAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body("{\nmessage\n:\nThe fundi details have been updated successfully\n}");
    }

    @PutMapping(path = "/password/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@RequestBody Account account, @PathVariable String id) {
        if (null == account.getPassword()) throw new CustomBadRequest("Password field is required");
        Account a = accountService.getAccountById(id);
        a.setPassword(encoder.encode(account.getPassword()));
        accountService.createOrUpdateAccount(a);
        return ResponseEntity.status(HttpStatus.OK).body("{\nmessage\n:\nPassword has been updated successfully\n}");
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).body("{\nmessage\n:\nThe Fundi account has been deleted successfully from the system\n}");
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getFundiById(String id) {
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


}
