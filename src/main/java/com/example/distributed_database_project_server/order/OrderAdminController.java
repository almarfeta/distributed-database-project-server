package com.example.distributed_database_project_server.order;

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

import com.example.distributed_database_project_server.domain.constant.OrderStatus;

import jakarta.validation.Valid;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(OrderAdminController.DEFAULT_ENDPOINT_MAPPING)
class OrderAdminController {

    static final String DEFAULT_ENDPOINT_MAPPING = "/api/admin/order";

    private final OrderService orderService;

    OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(this.orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody @Valid CreateOrderForm form) {
        UUID orderId = this.orderService.createOrder(form);
        return ResponseEntity.created(URI.create(DEFAULT_ENDPOINT_MAPPING + "/" + orderId)).body("Order created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        this.orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("orderId") UUID id, @RequestParam("status") OrderStatus status) {
        this.orderService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/delivery")
    public ResponseEntity<Void> updateDelivery(
            @PathVariable("orderId") UUID id,
            @RequestParam("shipper") String shipper,
            @RequestParam("awb") String awb
    ) {
        this.orderService.updateDelivery(id, shipper, awb);
        return ResponseEntity.noContent().build();
    }
}
