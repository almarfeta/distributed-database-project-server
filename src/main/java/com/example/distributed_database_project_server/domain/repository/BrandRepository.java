package com.example.distributed_database_project_server.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.BrandEntity;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {

    boolean existsByBrandName(String brandName);

    @Modifying
    @Query(value = "BEGIN REFRESH_BRANDS_REPLICAS(); END;", nativeQuery = true)
    void refreshReplicas();
}
