package com.example.distributed_database_project_server.product.brand;

import java.util.UUID;

import com.example.distributed_database_project_server.domain.entity.BrandEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class BrandResponse {

    private UUID brandId;
    private String brandName;

    private BrandResponse(UUID brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public static BrandResponse fromEntity(BrandEntity brand) {
        return new BrandResponse(brand.getId(), brand.getBrandName());
    }

    public UUID getBrandId() {
        return brandId;
    }

    public void setBrandId(UUID brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
