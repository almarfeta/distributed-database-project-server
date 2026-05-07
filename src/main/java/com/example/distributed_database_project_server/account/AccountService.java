package com.example.distributed_database_project_server.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.distributed_database_project_server.domain.constant.Role;
import com.example.distributed_database_project_server.domain.entity.AccountEntity;
import com.example.distributed_database_project_server.domain.entity.AddressEntity;
import com.example.distributed_database_project_server.domain.entity.CustomerEntity;
import com.example.distributed_database_project_server.domain.repository.AccountRepository;
import com.example.distributed_database_project_server.domain.repository.AddressRepository;
import com.example.distributed_database_project_server.domain.repository.CustomerRepository;
import com.example.distributed_database_project_server.domain.repository.TokenRepository;
import com.example.distributed_database_project_server.exception.BadRequestException;
import com.example.distributed_database_project_server.exception.NotFoundException;

import jakarta.transaction.Transactional;

@Service
class AccountService {

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    AccountService(
            AccountRepository accountRepository,
            TokenRepository tokenRepository,
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    List<AccountResponse> getAllAccounts() {
        return this.accountRepository.findAllWithCustomer().stream().map(AccountResponse::fromEntity).toList();
    }

    AccountResponse getAccountById(UUID id) {
        return this.accountRepository.findById(id)
                .map(AccountResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    @Transactional
    UUID createAccount(CreateAccountForm form) {
        if (this.accountRepository.existsByUsernameOrEmail(form.getUsername(), form.getEmail())) {
            throw new BadRequestException("Username or email already in use");
        }

        CustomerEntity customer = null;

        if (form.getRole() == Role.USER) {
            if (form.getFirstName() == null || form.getLastName() == null || form.getMarket() == null) {
                throw new BadRequestException("Market, firstname and lastname must not be null if the role is USER");
            }

            customer = this.customerRepository.save(new CustomerEntity(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getMarket(),
                    form.getDateOfBirth(),
                    form.getPhoneNumber()
            ));
        }

        return this.accountRepository.save(new AccountEntity(
                form.getUsername(),
                form.getEmail(),
                this.passwordEncoder.encode(form.getPassword()),
                form.getRole(),
                customer
        )).getId();
    }

    @Transactional
    void deleteAccount(UUID id) {
        this.tokenRepository.deleteAllByAccountId(id);
        this.accountRepository.deleteById(id);
    }

    List<AddressResponse> getAllAddresses() {
        return this.addressRepository.findAllWithCustomerAndAccount().stream().map(AddressResponse::fromEntity).toList();
    }

    List<AddressResponse> getAllAddressesByAccount(UUID id) {
        return this.addressRepository.findAllByAccountIdWithCustomerAndAccount(id).stream()
                .map(AddressResponse::fromEntity).toList();
    }

    AddressResponse getAddressById(UUID id) {
        return this.addressRepository.findById(id)
                .map(AddressResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Address not found"));
    }

    @Transactional
    UUID createAddress(UUID id, CreateAddressForm form) {
        Optional<AccountEntity> account = this.accountRepository.findById(id);

        if (account.isEmpty() || account.get().getCustomer() == null) {
            throw new NotFoundException("Customer account not found");
        }

        return this.addressRepository.save(new AddressEntity(
                form.getStreet(),
                form.getCity(),
                form.getRegion(),
                form.getCountry(),
                form.getPostalCode(),
                account.get().getCustomer()
        )).getId();
    }

    @Transactional
    void deleteAddress(UUID id) {
        this.addressRepository.deleteById(id);
    }
}
