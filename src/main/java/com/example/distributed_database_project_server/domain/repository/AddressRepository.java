package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.AddressEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {

    Optional<AddressEntity> findByIdAndCustomerId(UUID id, UUID customerId);

    @Query("select a from address a join fetch a.customer c join fetch c.account")
    List<AddressEntity> findAllWithCustomerAndAccount();

    @Query("select a from address a join fetch a.customer c join fetch c.account ac where ac.id = :accountId")
    List<AddressEntity> findAllByAccountIdWithCustomerAndAccount(@Param("accountId") UUID accountId);
}
