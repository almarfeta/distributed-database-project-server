package com.example.distributed_database_project_server.product.category;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

class CreateCategoryForm {

    @NotBlank(message = "Category name should not be blank")
    private String name;

    private UUID parentCategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(UUID parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
