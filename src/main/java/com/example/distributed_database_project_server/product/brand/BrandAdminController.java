package com.example.distributed_database_project_server.product.brand;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(BrandAdminController.DEFAULT_ENDPOINT_MAPPING)
class BrandAdminController {

    static final String DEFAULT_ENDPOINT_MAPPING = "/api/admin/brand";

    private final BrandService brandService;

    BrandAdminController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(this.brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.brandService.getBrandById(id));
    }

    @PostMapping
    public ResponseEntity<String> createBrand(@RequestBody @Valid CreateBrandForm form) {
        UUID brandId = this.brandService.createBrand(form);
        return ResponseEntity.created(URI.create(DEFAULT_ENDPOINT_MAPPING + "/" + brandId)).body("Brand created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) {
        this.brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
