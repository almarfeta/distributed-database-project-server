package com.example.distributed_database_project_server.product.category;

import java.util.UUID;

import com.example.distributed_database_project_server.domain.entity.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class CategoryResponse {

    private UUID categoryId;
    private UUID parentCategoryId;
    private String categoryName;

    private CategoryResponse(UUID categoryId, UUID parentCategoryId, String categoryName) {
        this.categoryId = categoryId;
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }

    public static CategoryResponse fromEntity(CategoryEntity category) {
        CategoryEntity parentCategory = category.getParentCategory();

        return new CategoryResponse(
                category.getId(),
                parentCategory != null ? parentCategory.getId() : null,
                category.getCategoryName()
        );
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(UUID parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
