package com.example.distributed_database_project_server.account;

import com.example.distributed_database_project_server.domain.constant.Market;
import com.example.distributed_database_project_server.domain.constant.Role;
import com.example.distributed_database_project_server.domain.entity.AccountEntity;
import com.example.distributed_database_project_server.domain.entity.CustomerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
class AccountResponse {

    private UUID accountId;
    private UUID customerId;
    private String username;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private Market market;
    private LocalDate dateOfBirth;
    private String phoneNumber;

    private AccountResponse(
            UUID accountId,
            UUID customerId,
            String username,
            String email,
            Role role,
            String firstName,
            String lastName,
            Market market,
            LocalDate dateOfBirth,
            String phoneNumber
    ) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.market = market;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public static AccountResponse fromEntity(AccountEntity account) {
        CustomerEntity customer = account.getCustomer();

        return new AccountResponse(
                account.getId(),
                customer != null ? customer.getId() : null,
                account.getUsername(),
                account.getEmail(),
                account.getRole(),
                customer != null ? customer.getFirstName() : null,
                customer != null ? customer.getLastName() : null,
                customer != null ? customer.getMarket() : null,
                customer != null ? customer.getDateOfBirth() : null,
                customer != null ? customer.getPhoneNumber() : null
        );
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Market getMarket() {
        return this.market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
