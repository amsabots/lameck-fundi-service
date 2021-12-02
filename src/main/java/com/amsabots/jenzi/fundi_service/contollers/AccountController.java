package com.amsabots.jenzi.fundi_service.contollers;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.services.AccountService;
import com.amsabots.jenzi.fundi_service.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * This request controller is only accessible by any user with system admin roles.
     * Should be configured to follow spring boot security configurations protocols and semantics
     * */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<Account>> getAllFundis(@RequestParam Optional<Integer> page,
                                                                @RequestParam Optional<Integer> pageSize) {
        int page_size = pageSize.orElse(10);
        int current_page = page.orElse(0);
        Pageable pageable = PageRequest.of(current_page, page_size);
        return ResponseEntity.ok(new ResponseObject<>(accountService.getAllFundis(pageable), page_size, current_page));
    }


}
