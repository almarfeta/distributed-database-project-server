package com.example.distributed_database_project_server.account;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(AccountAdminController.DEFAULT_ENDPOINT_MAPPING)
class AccountAdminController {

    static final String DEFAULT_ENDPOINT_MAPPING = "/api/admin/account";

    private final AccountService accountService;

    AccountAdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(this.accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody @Valid CreateAccountForm form) {
        UUID accountId = this.accountService.createAccount(form);
        return ResponseEntity.created(URI.create(DEFAULT_ENDPOINT_MAPPING + "/" + accountId)).body("Account created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        this.accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        return ResponseEntity.ok(this.accountService.getAllAddresses());
    }

    @GetMapping("/{accountId}/address")
    public ResponseEntity<List<AddressResponse>> getAllAddressesByAccount(@PathVariable("accountId") UUID id) {
        return ResponseEntity.ok(this.accountService.getAllAddressesByAccount(id));
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable("addressId") UUID id) {
        return ResponseEntity.ok(this.accountService.getAddressById(id));
    }

    @PostMapping("{accountId}/address")
    public ResponseEntity<String> addAddress(
            @PathVariable("accountId") UUID id,
            @RequestBody @Valid CreateAddressForm form
    ) {
        UUID addressId = this.accountService.createAddress(id, form);
        return ResponseEntity.created(URI.create(DEFAULT_ENDPOINT_MAPPING + "/address/" + addressId))
                .body("Address added");
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") UUID id) {
        this.accountService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
