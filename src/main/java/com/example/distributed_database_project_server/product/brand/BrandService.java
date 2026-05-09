package com.example.distributed_database_project_server.product.brand;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.distributed_database_project_server.domain.entity.BrandEntity;
import com.example.distributed_database_project_server.domain.repository.BrandRepository;
import com.example.distributed_database_project_server.exception.BadRequestException;
import com.example.distributed_database_project_server.exception.NotFoundException;

import jakarta.transaction.Transactional;

@Service
class BrandService {

    private final BrandRepository brandRepository;

    BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    List<BrandResponse> getAllBrands() {
        return this.brandRepository.findAll().stream().map(BrandResponse::fromEntity).toList();
    }

    BrandResponse getBrandById(UUID id) {
        return this.brandRepository.findById(id)
                .map(BrandResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
    }

    @Transactional
    UUID createBrand(CreateBrandForm form) {
        if (this.brandRepository.existsByBrandName(form.getName())) {
            throw new BadRequestException("Brand already exists");
        }

        UUID brandId = this.brandRepository.save(new BrandEntity(form.getName())).getId();

        this.brandRepository.refreshReplicas();

        return brandId;
    }

    @Transactional
    void deleteBrand(UUID id) {
        this.brandRepository.deleteById(id);
        this.brandRepository.refreshReplicas();
    }
}
