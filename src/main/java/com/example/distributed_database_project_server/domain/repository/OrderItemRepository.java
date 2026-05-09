package com.example.distributed_database_project_server.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

    void deleteAllByOrderId(UUID orderId);
}
