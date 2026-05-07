package com.example.distributed_database_project_server.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.distributed_database_project_server.domain.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("select distinct o from order o join fetch o.delivery join fetch o.orderItems")
    List<OrderEntity> findAllWithDeliveryAndOrderItems();
}
