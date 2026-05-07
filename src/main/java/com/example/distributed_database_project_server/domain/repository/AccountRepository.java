package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query("select a from account a left join fetch a.customer")
    List<AccountEntity> findAllWithCustomer();
}
