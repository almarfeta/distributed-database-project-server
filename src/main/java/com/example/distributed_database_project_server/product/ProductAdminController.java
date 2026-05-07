package com.example.distributed_database_project_server.product;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.distributed_database_project_server.domain.constant.ProductStatus;

import jakarta.validation.Valid;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(ProductAdminController.DEFAULT_ENDPOINT_MAPPING)
class ProductAdminController {

    static final String DEFAULT_ENDPOINT_MAPPING = "/api/admin/product";

    private final ProductService productService;

    ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid CreateProductForm form) {
        UUID productId = this.productService.createProduct(form);
        return ResponseEntity.created(URI.create(DEFAULT_ENDPOINT_MAPPING + "/" + productId)).body("Product created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @RequestParam("status") ProductStatus status) {
        this.productService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryResponse>> getAllInventories() {
        return ResponseEntity.ok(this.productService.getAllInventories());
    }

    @GetMapping("/{productId}/inventory")
    public ResponseEntity<List<InventoryResponse>> getAllInventoriesByProduct(@PathVariable("productId") UUID id) {
        return ResponseEntity.ok(this.productService.getAllInventoriesByProduct(id));
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable("inventoryId") UUID id) {
        return ResponseEntity.ok(this.productService.getInventoryById(id));
    }

    @PatchMapping("/inventory/{inventoryId}")
    public ResponseEntity<Void> updateStock(@PathVariable("inventoryId") UUID id, @RequestParam("stock") Integer stock) {
        this.productService.updateStock(id, stock);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/price")
    public ResponseEntity<List<PriceResponse>> getAllPrices() {
        return ResponseEntity.ok(this.productService.getAllPrices());
    }

    @GetMapping("/{productId}/price")
    public ResponseEntity<List<PriceResponse>> getAllPricesByProduct(@PathVariable("productId") UUID id) {
        return ResponseEntity.ok(this.productService.getAllPricesByProduct(id));
    }

    @GetMapping("/price/{priceId}")
    public ResponseEntity<PriceResponse> getPriceById(@PathVariable("priceId") UUID id) {
        return ResponseEntity.ok(this.productService.getPriceById(id));
    }

    @PostMapping("{productId}/price")
    public ResponseEntity<String> addPrice(
            @PathVariable("productId") UUID id,
            @RequestBody @Valid CreatePriceForm form
    ) {
        UUID priceId = this.productService.createPrice(id, form);
        return ResponseEntity.created(URI.create(DEFAULT_ENDPOINT_MAPPING + "/price/" + priceId))
                .body("Price added");
    }

    @DeleteMapping("/price/{priceId}")
    public ResponseEntity<Void> deletePrice(@PathVariable("priceId") UUID id) {
        this.productService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}
