package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.InventoryEntity;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    List<InventoryEntity> findAllByProductId(UUID productId);

    void deleteAllByProductId(UUID productId);
}
