package com.example.distributed_database_project_server.product;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

class CreateProductForm {

    @NotNull(message = "Brand ID should not be null")
    private UUID brandId;

    @NotNull(message = "Category ID should not be null")
    private UUID categoryId;

    @NotBlank(message = "SKU should not be blank")
    private String sku;

    @NotBlank(message = "Product name should not be blank")
    private String name;

    private String description;

    @NotNull(message = "Default price should not be null")
    @DecimalMin(value = "0.00", message = "Default price cannot be negative")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal defaultPrice;

    public UUID getBrandId() {
        return brandId;
    }

    public void setBrandId(UUID brandId) {
        this.brandId = brandId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }
}
