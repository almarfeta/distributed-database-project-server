package com.example.distributed_database_project_server.domain.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.distributed_database_project_server.domain.constant.WarehouseCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "inventory")
@Table(name = "inventories")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_code")
    private WarehouseCode warehouseCode;

    @Column(name = "stock_available")
    private Integer stockAvailable;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public InventoryEntity() {
    }

    public InventoryEntity(WarehouseCode warehouseCode, Integer stockAvailable, OffsetDateTime updatedAt, ProductEntity product) {
        this.warehouseCode = warehouseCode;
        this.stockAvailable = stockAvailable;
        this.updatedAt = updatedAt;
        this.product = product;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WarehouseCode getWarehouseCode() {
        return this.warehouseCode;
    }

    public void setWarehouseCode(WarehouseCode warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Integer getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(Integer stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
