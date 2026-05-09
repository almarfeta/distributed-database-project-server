package com.example.distributed_database_project_server.product.category;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.distributed_database_project_server.domain.entity.CategoryEntity;
import com.example.distributed_database_project_server.domain.repository.CategoryRepository;
import com.example.distributed_database_project_server.exception.NotFoundException;

import jakarta.transaction.Transactional;

@Service
class CategoryService {

    private final CategoryRepository categoryRepository;

    CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    List<CategoryResponse> getAllCategories() {
        return this.categoryRepository.findAll().stream().map(CategoryResponse::fromEntity).toList();
    }

    CategoryResponse getCategoryById(UUID id) {
        return this.categoryRepository.findById(id)
                .map(CategoryResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Transactional
    UUID createCategory(CreateCategoryForm form) {
        CategoryEntity parentCategory = null;

        if (form.getParentCategoryId() != null) {
            parentCategory = this.categoryRepository.findById(form.getParentCategoryId())
                    .orElseThrow(() -> new NotFoundException("Parent category not found"));
        }

        UUID categoryId = this.categoryRepository.save(new CategoryEntity(form.getName(), parentCategory)).getId();

        this.categoryRepository.refreshReplicas();

        return categoryId;
    }

    @Transactional
    void deleteCategory(UUID id) {
        this.categoryRepository.deleteById(id);
        this.categoryRepository.refreshReplicas();
    }
}
