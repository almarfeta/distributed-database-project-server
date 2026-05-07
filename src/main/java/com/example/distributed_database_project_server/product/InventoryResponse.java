package com.example.distributed_database_project_server.product;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.distributed_database_project_server.domain.constant.WarehouseCode;
import com.example.distributed_database_project_server.domain.entity.InventoryEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class InventoryResponse {

    private UUID productId;
    private UUID inventoryId;
    private WarehouseCode warehouseCode;
    private Integer stockAvailable;
    private OffsetDateTime updatedAt;

    private InventoryResponse(
            UUID productId,
            UUID inventoryId,
            WarehouseCode warehouseCode,
            Integer stockAvailable,
            OffsetDateTime updatedAt
    ) {
        this.productId = productId;
        this.inventoryId = inventoryId;
        this.warehouseCode = warehouseCode;
        this.stockAvailable = stockAvailable;
        this.updatedAt = updatedAt;
    }

    public static InventoryResponse fromEntity(InventoryEntity inventory) {
        return new InventoryResponse(
                inventory.getProduct().getId(),
                inventory.getId(),
                inventory.getWarehouseCode(),
                inventory.getStockAvailable(),
                inventory.getUpdatedAt()
        );
    }

    public UUID getProductId() {
        return this.productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }

    public WarehouseCode getWarehouseCode() {
        return this.warehouseCode;
    }

    public void setWarehouseCode(WarehouseCode warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Integer getStockAvailable() {
        return this.stockAvailable;
    }

    public void setStockAvailable(Integer stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
