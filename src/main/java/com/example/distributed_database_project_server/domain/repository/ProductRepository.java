package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("select p from product p join fetch p.brand join fetch p.category join fetch p.inventory")
    List<ProductEntity> findAllWithBrandAndCategoryAndInventory();
}
