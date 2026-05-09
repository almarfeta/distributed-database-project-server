package com.example.distributed_database_project_server.product;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.distributed_database_project_server.domain.constant.Currency;
import com.example.distributed_database_project_server.domain.constant.Market;
import com.example.distributed_database_project_server.domain.constant.PriceStatus;
import com.example.distributed_database_project_server.domain.constant.ProductStatus;
import com.example.distributed_database_project_server.domain.constant.WarehouseCode;
import com.example.distributed_database_project_server.domain.entity.BrandEntity;
import com.example.distributed_database_project_server.domain.entity.CategoryEntity;
import com.example.distributed_database_project_server.domain.entity.InventoryEntity;
import com.example.distributed_database_project_server.domain.entity.PriceEntity;
import com.example.distributed_database_project_server.domain.entity.ProductEntity;
import com.example.distributed_database_project_server.domain.repository.BrandRepository;
import com.example.distributed_database_project_server.domain.repository.CategoryRepository;
import com.example.distributed_database_project_server.domain.repository.InventoryRepository;
import com.example.distributed_database_project_server.domain.repository.PriceRepository;
import com.example.distributed_database_project_server.domain.repository.ProductRepository;
import com.example.distributed_database_project_server.exception.BadRequestException;
import com.example.distributed_database_project_server.exception.NotFoundException;

import jakarta.transaction.Transactional;

@Service
class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final PriceRepository priceRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    ProductService(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            PriceRepository priceRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.priceRepository = priceRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    List<ProductResponse> getAllProducts() {
        return this.productRepository.findAllWithBrandAndCategory().stream()
                .map(ProductResponse::fromEntity).toList();
    }

    ProductResponse getProductById(UUID id) {
        return this.productRepository.findById(id)
                .map(ProductResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Transactional
    UUID createProduct(CreateProductForm form) {
        BrandEntity brand = this.brandRepository.findById(form.getBrandId())
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        CategoryEntity category = this.categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        ProductEntity product = this.productRepository.save(new ProductEntity(
                form.getSku(),
                form.getName(),
                form.getDescription(),
                ProductStatus.INACTIVE,
                brand,
                category
        ));

        for (WarehouseCode warehouseCode : WarehouseCode.values()) {
            this.inventoryRepository.save(new InventoryEntity(
                    warehouseCode,
                    0,
                    OffsetDateTime.now(),
                    product
            ));
        }

        for (Market market : Market.values()) {
            this.priceRepository.save(new PriceEntity(
                    market,
                    form.getDefaultPrice(),
                    Currency.EUR,
                    PriceStatus.DEFAULT,
                    product
            ));
        }

        return product.getId();
    }

    @Transactional
    void updateStatus(UUID id, ProductStatus status) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setStatus(status);
    }

    @Transactional
    void deleteProduct(UUID id) {
        this.priceRepository.deleteAllByProductId(id);
        this.inventoryRepository.deleteAllByProductId(id);
        this.productRepository.deleteById(id);
    }

    List<InventoryResponse> getAllInventories() {
        return this.inventoryRepository.findAll().stream().map(InventoryResponse::fromEntity).toList();
    }

    List<InventoryResponse> getAllInventoriesByProduct(UUID id) {
        return this.inventoryRepository.findAllByProductId(id).stream().map(InventoryResponse::fromEntity).toList();
    }

    InventoryResponse getInventoryById(UUID id) {
        return this.inventoryRepository.findById(id)
                .map(InventoryResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));
    }

    @Transactional
    void updateStock(UUID id, Integer stock) {
        InventoryEntity inventory = this.inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        inventory.setStockAvailable(stock);
    }

    List<PriceResponse> getAllPrices() {
        return this.priceRepository.findAll().stream().map(PriceResponse::fromEntity).toList();
    }

    List<PriceResponse> getAllPricesByProduct(UUID id) {
        return this.priceRepository.findAllByProductId(id).stream().map(PriceResponse::fromEntity).toList();
    }

    PriceResponse getPriceById(UUID id) {
        return this.priceRepository.findById(id)
                .map(PriceResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Price not found"));
    }

    @Transactional
    UUID createPrice(UUID id, CreatePriceForm form) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.getPrices().stream()
                .filter(price -> price.getMarket() == form.getMarket())
                .filter(price -> price.getStatus() == PriceStatus.ACTIVE)
                .forEach(price -> price.setStatus(PriceStatus.INACTIVE));

        return this.priceRepository.save(new PriceEntity(
                form.getMarket(),
                form.getPrice(),
                form.getCurrency(),
                PriceStatus.ACTIVE,
                product
        )).getId();
    }

    @Transactional
    void deletePrice(UUID id) {
        Optional<PriceEntity> price = this.priceRepository.findById(id);

        if (price.isPresent() && price.get().getStatus() == PriceStatus.DEFAULT) {
            throw new BadRequestException("Cannot delete price because price status is DEFAULT");
        }

        this.priceRepository.deleteById(id);
    }
}
