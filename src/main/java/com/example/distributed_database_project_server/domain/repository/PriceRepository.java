package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.PriceEntity;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, UUID> {

    Optional<PriceEntity> findByIdAndProductId(UUID id, UUID productId);

    void deleteAllByProductId(UUID productId);

    List<PriceEntity> findAllByProductId(UUID productId);
}
