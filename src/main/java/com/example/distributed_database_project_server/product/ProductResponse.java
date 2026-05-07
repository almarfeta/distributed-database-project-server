package com.example.distributed_database_project_server.product;

import java.util.UUID;

import com.example.distributed_database_project_server.domain.constant.ProductStatus;
import com.example.distributed_database_project_server.domain.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class ProductResponse {

    private UUID productId;
    private String sku;
    private String productName;
    private String description;
    private ProductStatus status;
    private String brand;
    private String category;

    private ProductResponse(
            UUID productId,
            String sku,
            String productName,
            String description,
            ProductStatus status,
            String brand,
            String category
    ) {
        this.productId = productId;
        this.sku = sku;
        this.productName = productName;
        this.description = description;
        this.status = status;
        this.brand = brand;
        this.category = category;
    }

    public static ProductResponse fromEntity(ProductEntity product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getProductName(),
                product.getDescription(),
                product.getStatus(),
                product.getBrand().getBrandName(),
                product.getCategory().getCategoryName()
        );
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
